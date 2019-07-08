var AWS = require("aws-sdk");


AWS.config.update({ region: "us-west-2" });
var docClient = new AWS.DynamoDB.DocumentClient();

export class EvendtDao {
    getCompanyAppEvents(companyId: string, event:string) {
        return new Promise(async function (resolve, reject) {
            
            const params = {
                TableName: 'eventbus-companyAppEvent',
                IndexName: 'companyId-event-index',
                KeyConditionExpression: 'companyId = :companyId and event = :event',
                ExpressionAttributeValues:{
                    ":companyId": companyId,
                    ":event":event        
                }
            }

            docClient.query(params, function (err, data) {
                if (err) {
                    console.log(err)
                    reject(err);
                } else {
                    resolve(data.Items);
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

}