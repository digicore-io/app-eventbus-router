import "source-map-support/register";
import { QueueService } from "./service/queue-service";
import { Router } from "./router";
import { BaseService } from "@digicore-io/digicore-node-common-module/lib/base-service";
import { AwsHttpHandler } from "@digicore-io/digicore-node-common-module/lib/aws-http-handler";

var httpHandler = new AwsHttpHandler();
var queueService = new QueueService();
var baseService = new BaseService();
const API = "Eventbus Router";
const SLACK_CHANNEL = "devops-event-bus";

const AWS = require("aws-sdk");

export const router = async (event, context) => {
  if (event.headers) {
    //HTTP Message
    return httpHandler.handle(event, context, new Router(), API, SLACK_CHANNEL);
  } else {
    //SQS Message
    try {
      await queueService.processMessages(event);
    } catch (err) {
      baseService.logToSlack("devops-event-bus", "EventBus Router", err);
    }
  }
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
