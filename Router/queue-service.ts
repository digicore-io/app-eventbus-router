import { EvendtDao } from "./dao/event-dao";

const AWS = require('aws-sdk');
AWS.config.update({ region: 'us-west-2' });
const ddb = new AWS.DynamoDB.DocumentClient();
const sqs = new AWS.SQS({ apiVersion: '2012-11-05' });
const uuidv4 = require('uuid/v4');
const eventDao = new EvendtDao();

export class QueueService {
    async processMessages(event) {
        for (const item of event.Records)
            await this.processMessage(item);
    }

    async processMessage(item: any) {
        console.log('BODY', item.body)
        let eventRequest: EventRequest = <EventRequest>JSON.parse(item.body);
        console.log('Event Request', eventRequest)
        if(!eventRequest.event || !eventRequest.companyId || !eventRequest.payload){
            console.error('Event, Company ID or Payload was not specified')
            return;
        }

        console.log('Received event. Event ' + eventRequest.event + ' + companyId ' + eventRequest.companyId);
        let appEvents: any = await eventDao.getCompanyAppEvents(eventRequest.companyId, eventRequest.event);


        //Company may have multiple apps registered for the same event
        for (let appEvent of appEvents) {
            try {
                const app: Application = <Application>await eventDao.getApplication(appEvent.applicationId)
                await this.routeRequestToApp(eventRequest, appEvent, app);
            } catch (error) {
                console.log('CAUGHT ERROR');
                console.log('Event Request', eventRequest)
                await eventDao.logError(eventRequest, error);
            }
        }

    }

    async routeRequestToApp(eventRequest: EventRequest, appEvent: CompanyAppEvent, app: Application) {
        switch (app.destinationType) {
            case DestinationType.SQS: {
                eventRequest.response = await this.sendSQS(eventRequest, appEvent, app);
                await eventDao.logEventRequest(eventRequest, app.id);
                break;
            }
            default: {
                throw new Error('Error. Unsupported destination. EventRequest ' + JSON.stringify(eventRequest) + ' Application: ' + JSON.stringify(app))
            }
        }

    }

    async sendSQS(eventRequest: EventRequest, appEvent: CompanyAppEvent, app: Application) {
        return new Promise(async function (resolve, reject) {
            var params = {
                MessageBody: JSON.stringify(eventRequest),
                QueueUrl: app.destinationUrl
            };

            console.log('Sending SQS to ' + params.QueueUrl)
            sqs.sendMessage(params, function (err, data) {
                console.log('err', err)
                if (err) {
                    console.log(err)
                    reject(err)
                } else {

                    console.log(data)
                    resolve(data);
                }
            });
        });
    }
}

export class EventRequest {
    companyId: string;
    event: string;
    applicationId: string;
    payload: any;
    response: any;
}

export class CompanyAppEvent {
    id: string;
    companyId: string;
    event: string;
    applicationId: string;
    config: any;
}

export class Application {
    id: string;
    destinationType: DestinationType;
    destinationUrl: string;
    destinationArn: string;
}

export const enum DestinationType {
    SQS = 'SQS',
}