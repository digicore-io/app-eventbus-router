
import {Location, LocationRequest} from '../classes';
import {Params, Error} from '../classes'
import { AppEntity } from '../interfaces';
const AWS = require('aws-sdk');

const docClient = new AWS.DynamoDB.DocumentClient({region: 'us-west-2'});

export class AppEntityDao {
    saveAppEntity(appId: string, companyId: string, entityId: string, appEntity:AppEntity) {
      return new Promise(async function(resolve, reject) {
        try {
          appEntity.appId = appId;
          appEntity.companyAndEntityId = companyId + '-' + entityId;
          let params = new Params();
          params.TableName = "eventbus-appEntity";
          params.Item = appEntity
          
          
          docClient.put(params, function(err,data){
            if(err){
                reject(err);
                throw 'Error: ' + JSON.stringify(err, null, 2);
            }else{
                resolve();
            }
          });
      } catch (err) {
        console.log('Error occurred', err);
        reject(err);
     }
    });
    }

    getAppEntity(appId, companyId: string, entityId:string) {
      return new Promise(async function(resolve, reject) {
          try {
            let params = new Params();
            params.TableName = "eventbus-appEntity";
            params.Key = {"appId":appId, "companyAndEntityId":companyId + '-' + entityId}
            
                        
            docClient.get(params, function(err,data){
              if(err){
                  reject(err);
                  throw 'Error: ' + JSON.stringify(err, null, 2);
              }else{
                  if(data.Item)
                    resolve(data.Item);
                  else
                    reject(new Error(404, "Not Found"));
              }
            });
        } catch (err) {
          console.log('Error occurred', err);
          reject(err);
       }
      });
    } 
  
  }



 