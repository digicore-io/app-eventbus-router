import { DataReplicationDao } from "../dao/data-replication-dao";
import { DestinationType, Table } from "../classes";

const AWS = require("aws-sdk");
const ddb = new AWS.DynamoDB.DocumentClient();
const sqs = new AWS.SQS({ apiVersion: "2012-11-05" });
const uuidv4 = require("uuid/v4");
const dao = new DataReplicationDao();

export class ReplicatorService {
  async processDbActions(records: Array<any>) {
    let dbActions: any = JSON.parse(records[0].body);
    let tableAction = dbActions[0];

    console.log("Checking for table " + tableAction.table);
    let table: Table = <Table>await dao.getTable(tableAction.table);

    console.log("Table ", table);
    if (!table) {
      console.log("Table is not replicated");
      return;
    }

    await this.routeReplicationToApp(table, records);
  }

  private async routeReplicationToApp(table: Table, records: Array<any>) {
    if (!table.destinationUrl) throw new Error("Table " + table.tableName + " is monitored but it has not destinationUrl");

    if (!table.destinationType) throw new Error("Table " + table.tableName + " is monitored but it has not destinationType");

    switch (table.destinationType) {
      case DestinationType.SQS: {
        await this.sendSQS(table, records);
        break;
      }
      default: {
        throw new Error("Error. Unsupported destination. Data Replication Request. Table: " + table.tableName);
      }
    }

    console.log("Routed replication to app");
  }

  private async sendSQS(table: Table, records: Array<any>) {
    return new Promise(async function(resolve, reject) {
      var params = {
        MessageBody: JSON.stringify({ messageType: "replication", body: records }),
        QueueUrl: table.destinationUrl
      };

      console.log("Sending SQS to " + params.QueueUrl);
      sqs.sendMessage(params, function(err, data) {
        console.log("err", err);
        if (err) {
          console.log(err);
          reject(err);
        } else {
          console.log(data);
          resolve(data);
        }
      });
    });
  }
}
