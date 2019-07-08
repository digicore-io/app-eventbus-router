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
        
        let eventRequest: EventRequest = <EventRequest>item.body;
        console.log('Received event. Event ' + eventRequest.event + ' + companyId ' + eventRequest.companyId);
        let appEvents:any = await eventDao.getCompanyAppEvents(eventRequest.companyId, eventRequest.event);
        
        
        //Company may have multiple apps registered for the same event
        for (let appEvent of appEvents){
            console.log('GETTING APP')
            const app:Application = <Application>await eventDao.getApplication(appEvent.applicationId)
            this.routeRequestToApp(eventRequest, appEvent, app);
        }

    }

    routeRequestToApp(eventRequest: EventRequest, appEvent: CompanyAppEvent, app: Application) {
        console.log('destination type, ', app.destinationType)
        switch (app.destinationType) {
            case DestinationType.SQS: {
                this.sendSQS(eventRequest, appEvent, app);
                break;
            }
            default: {
                console.log('Error. Unsupported destination. EventRequest ' + JSON.stringify(eventRequest) + ' Application: ' + JSON.stringify(app))
            }
        }

    }

    async sendSQS(eventRequest: EventRequest, appEvent: CompanyAppEvent, app: Application) {
        var params = {
            MessageBody: JSON.stringify(eventRequest),
            QueueUrl: app.destinationUrl,
        };

        var response = await sqs.sendMessage(params).promise();
        var log: any = { TableName: 'eventbus-eventLog', Key: { id: uuidv4() } };
        log.eventRequest = eventRequest;

        ddb.save(log), function (err, data) {
            if (err)
                console.log(err)
            else
                console.log("Event has been logged");
        };
    }
}

export class EventRequest {
    companyId: string;
    event: string;
    applicationId: string;
    payload: any;
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

export enum DestinationType {
    SQS
}