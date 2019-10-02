import { HttpError } from "@DigiProMedia/digicore-node/lib/common-classes";
import { AppEntity } from "../interfaces";
import { Params } from "../classes";
const AWS = require("aws-sdk");

const docClient = new AWS.DynamoDB.DocumentClient({ region: "us-east-1" });

export class AppEntityDao {
  saveAppEntity(applicationId: string, companyId: string, entityId: string, appEntity: AppEntity) {
    return new Promise(async function(resolve, reject) {
      try {
        appEntity.applicationId = applicationId;
        appEntity.companyAndEntityId = companyId + "-" + entityId;
        let params = new Params();
        params.TableName = "eventbus-appEntity";
        params.Item = appEntity;

        docClient.put(params, function(err, data) {
          if (err) {
            console.log("test err");
            reject(err);
          } else {
            resolve(appEntity);
          }
        });
      } catch (err) {
        console.log("Error occurred", err);
        reject(err);
      }
    });
  }

  getAppEntity(applicationId, companyId: string, entityId: string) {
    return new Promise(async function(resolve, reject) {
      try {
        let params = new Params();
        params.TableName = "eventbus-appEntity";
        params.Key = { applicationId: applicationId, companyAndEntityId: companyId + "-" + entityId };

        docClient.get(params, function(err, data) {
          if (err) {
            reject(err);
          } else {
            if (data.Item) resolve(data.Item);
            else reject(new HttpError(404, "Not Found"));
          }
        });
      } catch (err) {
        console.log("Error occurred", err);
        reject(err);
      }
    });
  }
}
