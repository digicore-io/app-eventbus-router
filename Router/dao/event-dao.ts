var AWS = require("aws-sdk");


AWS.config.update({ region: "us-west-2" });
var docClient = new AWS.DynamoDB.DocumentClient();

export class EvendtDao {
    getCompanyAppEvents(companyId: string, event:string) {
        return new Promise(async function (resolve, reject) {
            
            const params = {
                TableName: 'eventbus-companyAppEvent',
                IndexName: 'companyId-event-index',
                Key: { 'companyId': companyId, ':event': event }
            }

            
            docClient.get(params, function (err, data) {
                console.log('Got data data')
                if (err) {
                    console.log('err')
                    console.log(err)
                    reject(err);
                } else {
                    console.log('data')
                    console.log(data)
                    resolve(data);
                }
            });

            console.log('returning')
        });
    }

    getApplication(id: string) {
        return new Promise(async function (resolve, reject) {
            
            const params = { TableName: 'eventbus-application3', Key: { id: id } };
            docClient.get(params), function (err, data) {
                console.log('application')
                if (err) {
                    reject(err);
                } else {
                    console.log('test appp')
                    resolve(data);
                }
            };

        });
    }

}