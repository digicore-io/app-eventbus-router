import { Router } from "./router";
import { AwsHttpHandler } from "@digicore-io/digicore-node-common-module/lib/aws-http-handler";

let httpHandler = new AwsHttpHandler();
const API = "EventBus App Manager";
const SLACK_CHANNEL = "devops-event-bus";
const STAGE = process.env == null ? "local" : process.env;

export const handle = async (event, context: any) => {
  let router = new Router();

  return await httpHandler.handle(event, context, new Router(), API, SLACK_CHANNEL);
};