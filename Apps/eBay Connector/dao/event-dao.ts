import { EventRequest, CompanyAppConfig } from "../classes";


var AWS = require("aws-sdk");
const uuidv4 = require('uuid/v4');

AWS.config.update({ region: "us-west-2" });
var docClient = new AWS.DynamoDB.DocumentClient();

export class EvendtDao {
    updateCompanyAppConfig(config: CompanyAppConfig) {
        return new Promise(async function (resolve, reject) {
            
            const params = {TableName: 'eventbus-companyApp', 
            Key: { "companyId": config.companyId, "applicationId":config.applicationId},
                Item: { config}};

            console.log('updating', config)
            docClient.update(params, function (err, data) {
                if (err) {
                    console.log(err)
                    reject(err);
                } else {
                    //console.log(data)
                    resolve();
                }
            });
        });
    }
    getCompanyAppConfig(companyId:string, applicationId) {
        return new Promise(async function (resolve, reject) {
            console.log(companyId + '  ' + applicationId)
            const params = {TableName: 'eventbus-companyApp', Key: { "companyId": companyId, "applicationId":applicationId}};

            docClient.get(params, function (err, data) {
                if (err) {
                    console.log(err)
                    reject(err);
                } else {
                    //console.log(data)
                    if(data.Item)
                        resolve(data.Item);
                    else
                        reject(new Error("CompanyApp Not found"))
                }
            });
        });
    }

    getApplication(applicationId: string) {
        return new Promise(async function (resolve, reject) {
            const params = { TableName: 'eventbus-application', Key: { "id": applicationId} };
            docClient.get(params, function (err, data) {
                if (err) {
                    reject(err);
                } else {
                    if(data.Item)
                        resolve(JSON.parse(data.Item.config));
                    else
                        reject(new Error("Could not find Application"))
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