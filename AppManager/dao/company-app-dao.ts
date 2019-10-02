import { Params } from "../classes";

const AWS = require("aws-sdk");
const docClient = new AWS.DynamoDB.DocumentClient({ region: "us-east-1" });

export class CompanyAppDao {
  getCompanyApp(applicationId: string, companyId: string) {
    return new Promise(async function(resolve, reject) {
      try {
        let params = new Params();
        params.TableName = "eventbus-companyApp";
        params.Key = { applicationId: applicationId, companyId: companyId };

        docClient.get(params, function(err, data) {
          if (err) {
            reject(err);
          } else {
            if (data.Item) {
              let companyApp = data.Item;
              companyApp.config = JSON.parse(companyApp.config);
              resolve(companyApp);
            } else resolve();
          }
        });
      } catch (err) {
        console.log("Error occurred", err);
        reject(err);
      }
    });
  }

  getCompanyAppEvents(companyId: string, event: string) {
    return new Promise(async function(resolve, reject) {
      const params = {
        TableName: "eventbus-companyAppEvent",
        IndexName: "companyId-event-index",
        KeyConditionExpression: "companyId = :companyId and event = :event",
        ExpressionAttributeValues: {
          ":companyId": companyId,
          ":event": event
        }
      };

      docClient.query(params, function(err, data) {
        if (err) {
          reject(err);
        } else {
          resolve(data.Items);
        }
      });
    });
  }

  saveCompanyApp(applicationId: string, companyId: string, config: any) {
    return new Promise(async function(resolve, reject) {
      try {
        let companyApp: any = {};

        companyApp.applicationId = applicationId;
        companyApp.companyId = companyId;
        companyApp.config = JSON.stringify(config);

        let params = new Params();
        params.TableName = "eventbus-companyApp";
        params.Item = companyApp;

        docClient.put(params, function(err, data) {
          if (err) {
            reject(err);
          } else {
            resolve(companyApp);
          }
        });
      } catch (err) {
        console.log("Error occurred", err);
        reject(err);
      }
    });
  }
}
