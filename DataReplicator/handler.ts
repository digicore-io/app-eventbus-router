import "source-map-support/register";
import { Router } from "./router";
import { BaseService } from "@DigiProMedia/digicore-node/lib/base-service";
import { AwsHttpHandler } from "@DigiProMedia/digicore-node/lib/aws-http-handler";
import { ReplicatorService } from "./service/replicator-service";

var httpHandler = new AwsHttpHandler();
var service = new ReplicatorService();
var baseService = new BaseService();
const API = "Eventbus Router";
const SLACK_CHANNEL = "devops-event-bus";

const AWS = require("aws-sdk");

export const router = async (event, context) => {
  try {
    await service.processDbActions(event.Records);
  } catch (err) {
    console.log(err);
    baseService.logToSlack("devops-event-bus", "EventBus Router", err);
  }
  // for (const item of event.Records) {
  //   console.log("BODY ", item.body);

  //   let dbActions: any = JSON.parse(item.body);
  //   console.log("Body Len " + dbActions.length);
  //   for (const dbAction of dbActions) {
  //     console.log("User ID " + dbAction.userId);
  //     console.log("SQL " + dbAction.sql);
  //   }
  // }

  // let obj: any = JSON.parse(event.body);
  // console.log("User ID " + obj.userId);
  // console.log("SQL " + obj.sql);
  // if (event.headers) {
  //   //HTTP Message
  //   return httpHandler.handle(event, context, new Router(), API, SLACK_CHANNEL);
  // } else {
  //   //SQS Message
  //   try {
  //     await queueService.processMessages(event);
  //   } catch (err) {
  //     baseService.logToSlack("devops-event-bus", "EventBus Router", err);
  //   }
  // }
};

function getResponse(body: any) {
  return {
    statusCode: body.status,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Credentials": true
    },
    body: JSON.stringify(body)
  };
}
