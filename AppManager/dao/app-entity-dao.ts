import { HttpError } from "@digicore-io/digicore-node-common-module/lib/common-classes";
import { AppEntity } from "../interfaces";
import { Params } from "../classes";
const AWS = require("aws-sdk");

const docClient = new AWS.DynamoDB.DocumentClient();

export class AppEntityDao {
  /**
   *
   * @param applicationId
   * @param companyId
   * @param digicoreEntityId (DMD Entity ID)
   * @param appEntity
   */
  saveAppEntity(applicationId: string, companyId: string, digicoreEntityId: string, appEntity: AppEntity) {
    return new Promise(async function(resolve, reject) {
      try {
        appEntity.applicationId = applicationId;
        appEntity["companyId-digicoreEntityId"] = companyId + "-" + digicoreEntityId;
        let params = new Params();
        params.TableName = "eventbus-appEntity";
        params.Item = appEntity;

        docClient.put(params, function(err, data) {
          if (err) {
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

  getAppEntity(applicationId, companyId: string, digicoreEntityId: string) {
    return new Promise(async function(resolve, reject) {
      try {
        let params = new Params();
        params.TableName = "eventbus-appEntity";
        params.Key = { applicationId: applicationId, "companyId-digicoreEntityId": companyId + "-" + digicoreEntityId };

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

  getAppEntityByExternalId(applicationId: string, companyId: string, externalEntityId: string) {
    return new Promise(async function(resolve, reject) {
      try {
        let params = new Params();
        params.TableName = "eventbus-appEntity";
        params.IndexName = "companyId-externalEntityId-index";
        params.KeyConditionExpression = "companyId = :companyId and externalEntityId = :externalEntityId";
        params.ExpressionAttributeValues = {
          ":companyId": companyId,
          ":externalEntityId": externalEntityId
        };

        docClient.query(params, function(err, data) {
          if (err) {
            reject(err);
          } else {
            if (data.Items && data.Items.length > 0) resolve(data.Items[0]);
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
