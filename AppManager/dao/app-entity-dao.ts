
import {Location, LocationRequest} from '../classes';
import {Params, Error} from '../classes'
import { AppEntity } from '../interfaces';
const AWS = require('aws-sdk');

const docClient = new AWS.DynamoDB.DocumentClient({region: 'us-west-2'});

export class AppEntityDao {
    saveAppEntity(appEntity:AppEntity) {
      return new Promise(async function(resolve, reject) {
        try {
          
          let params = new Params();
          params.TableName = "eventbus-app-entity";
          params.Item = appEntity
          
          
          docClient.put(params, function(err,data){
            if(err){
                reject(err);
                throw 'Error: ' + JSON.stringify(err, null, 2);
            }else{
                resolve(data);
            }
          });
      } catch (err) {
        console.log('Error occurred', err);
        reject(err);
     }
    });
    }

    getAppEntity(entityId:string, companyId: string) {
      return new Promise(async function(resolve, reject) {
          try {
            let params = new Params();
            params.TableName = "eventbus-app-entity";
            params.Key = {"entityId":entityId, "companyId":companyId}
            
                        
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



 