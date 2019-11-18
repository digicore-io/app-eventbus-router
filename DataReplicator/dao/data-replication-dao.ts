var AWS = require("aws-sdk");
const uuidv4 = require("uuid/v4");

var docClient = new AWS.DynamoDB.DocumentClient();

export class DataReplicationDao {
  getTable(tableName: string) {
    return new Promise(async function(resolve, reject) {
      const params = { TableName: "eventbus-dataReplication", Key: { tableName: tableName } };

      docClient.get(params, function(err, data) {
        if (err) {
          reject(err);
        } else {
          resolve(data.Item);
        }
      });
    });
  }
}
