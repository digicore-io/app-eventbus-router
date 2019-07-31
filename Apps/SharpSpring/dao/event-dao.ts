import { EventRequest, CompanyAppEvent } from "../sharpspring-service";

var AWS = require("aws-sdk");
const uuidv4 = require('uuid/v4');

AWS.config.update({ region: "us-west-2" });
var docClient = new AWS.DynamoDB.DocumentClient();

export class EvendtDao {
    getCompanyAppEvent(companyAppEventId:string) {
        return new Promise(async function (resolve, reject) {
            
            const params = {TableName: 'eventbus-companyAppEvent', Key: { "id": companyAppEventId}};

            docClient.get(params, function (err, data) {
                if (err) {
                    console.log(err)
                    reject(err);
                } else {
                    resolve(data.Item);
                }
            });
        });
    }

    getApplication(applicationId: string) {
        return new Promise(async function (resolve, reject) {
            const params = { TableName: 'eventbus-application', Key: { "id": applicationId} };
            docClient.get(params, function (err, data) {
                if (err) {
                    console.log(err)
                    reject(err);
                } else {
                    resolve(data.Item);
                }
            });

        });
    }

    logEventRequest(eventRequest: EventRequest, applicationId:string) {
        return new Promise(async function (resolve, reject) {
            var log: any = { TableName: 'eventbus-eventLog', 
                Item:{
                    id: uuidv4(),
                    applicationId: applicationId,
                    eventRequest : JSON.stringify(eventRequest),
                    status : 'OK'
                }
            };
            
            docClient.put(log, function (err, data) {
                if (err)
                    reject(err);
                else
                    resolve(data);
            });
        });
    }

    logError(eventRequest: EventRequest, error: any) {
        return new Promise(async function (resolve, reject) {
            var log: any = { TableName: 'eventbus-eventLog', 
                Item:{
                    id: uuidv4(),
                    eventRequest : JSON.stringify(eventRequest),
                    error: JSON.stringify(error),
                    status : 'ERROR'
                }
            };
            
            docClient.put(log, function (err, data) {
                
                if (err)
                    reject(err);
                else
                    resolve(error);
            });
        });
    }
}