import { EventRequest } from "../classes";

var AWS = require("aws-sdk");
const uuidv4 = require("uuid/v4");

AWS.config.update({ region: "us-east-1" });
var docClient = new AWS.DynamoDB.DocumentClient();

export class EvendtDao {
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
          console.log(err);
          reject(err);
        } else {
          resolve(data.Items);
        }
      });
    });
  }

  getApplication(applicationId: string) {
    return new Promise(async function(resolve, reject) {
      const params = { TableName: "eventbus-application", Key: { id: applicationId } };

      docClient.get(params, function(err, data) {
        if (err) {
          reject(err);
        } else {
          resolve(data.Item);
        }
      });
    });
  }

  logEventRequest(eventRequest: EventRequest, applicationId: string) {
    return new Promise(async function(resolve, reject) {
      var log: any = {
        TableName: "eventbus-eventLog",
        Item: {
          id: uuidv4(),
          applicationId: applicationId,
          eventRequest: JSON.stringify(eventRequest),
          status: "OK"
        }
      };

      docClient.put(log, function(err, data) {
        if (err) reject(err);
        else resolve(data);
      });
    });
  }

  logError(eventRequest: EventRequest, error: any) {
    return new Promise(async function(resolve, reject) {
      var log: any = {
        TableName: "eventbus-eventLog",
        Item: {
          id: uuidv4(),
          eventRequest: JSON.stringify(eventRequest),
          error: JSON.stringify(error),
          status: "ERROR"
        }
      };

      docClient.put(log, function(err, data) {
        if (err) reject(err);
        else resolve(error);
      });
    });
  }
}
