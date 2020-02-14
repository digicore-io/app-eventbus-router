import { EvendtDao } from "../dao/event-dao";
import { CompanyAppEvent, EventRequest, Application } from "../classes";
import { BaseService } from "@digicore-io/digicore-node-common-module/lib/base-service";
import { QueueService } from "./queue-service";
import { HttpError, Route } from "@digicore-io/digicore-node-common-module/lib/common-classes";

const AWS = require("aws-sdk");

const ddb = new AWS.DynamoDB.DocumentClient();
const eventDao = new EvendtDao();
const queueService = new QueueService();

export class ApiService extends BaseService {
  async processApiRequest(event: any, route: Route) {
    this.validateParam(route, "companyId");
    this.validateParam(route, "event");
    this.validateParam(route, "applicationId");
    this.validateParam(route, "token");

    console.log("Looking for companyAppEvents");
    let companyAppEvents: any = await eventDao.getCompanyAppEvents(route.queryParameters.companyId, route.queryParameters.event);

    console.log("Number of events found for company: " + companyAppEvents.length);

    if (companyAppEvents.length == 0)
      this.logToSlack(
        "devops-event-bus",
        "Eventbus Router",
        "Received external event for a company that doesn't have event configured." +
          " Event (" +
          route.queryParameters.event +
          ")" +
          " Company ID (" +
          route.queryParameters.companyId +
          ")"
      );

    for (let appEvent of companyAppEvents) {
      console.log("Handling request for " + appEvent.applicationId + " " + route.queryParameters.applicationId);
      if (appEvent.applicationId == route.queryParameters.applicationId) await this.handleRequest(event, appEvent, route);
      else console.log("Application IDs don't match");
    }
  }

  async handleRequest(event: any, appEvent: CompanyAppEvent, route: Route) {
    console.log("test");
    if (appEvent.authToken != route.queryParameters.token) throw new HttpError(400, "Forbidden");

    let eventRequest: EventRequest = new EventRequest();
    eventRequest.applicationId = route.queryParameters.applicationId;
    eventRequest.companyAppEventId = appEvent.id;
    eventRequest.companyId = route.queryParameters.companyId;
    eventRequest.event = route.queryParameters.event;
    eventRequest.payload = event.body;

    console.log("Routing request");

    let app: any = await eventDao.getApplication(route.queryParameters.applicationId);

    await queueService.routeRequestToApp(eventRequest, appEvent, <Application>app);

    console.log("Routed request to application " + eventRequest.applicationId);
  }
}
