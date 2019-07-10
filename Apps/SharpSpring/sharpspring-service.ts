import { EvendtDao } from "./dao/event-dao";

const AWS = require('aws-sdk');
AWS.config.update({ region: 'us-west-2' });
const ddb = new AWS.DynamoDB.DocumentClient();
const sqs = new AWS.SQS({ apiVersion: '2012-11-05' });
const uuidv4 = require('uuid/v4');
const eventDao = new EvendtDao();

export class SharpSpringService {
    async processMessages(event) {
        for (const item of event.Records)
            await this.processMessage(item);
    }

    async processMessage(item: any) {
        console.log('BODY', item.body)
        let eventRequest: EventRequest = <EventRequest>JSON.parse(item.body);
        console.log('Event Request', eventRequest)
        
    }

}

export class EventRequest {
    companyId: string;
    event: string;
    applicationId: string;
    payload: any;
    response: any;
    companyAppEventId:string;
}
