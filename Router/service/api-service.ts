import { EvendtDao } from "../dao/event-dao";
import { CompanyAppEvent, EventRequest, Application } from "../classes";
import { BaseService } from "../../../NodeJS-Common/base-service";
import { QueueService } from "./queue-service";
import { HttpError, Route } from "../../../NodeJS-Common/common-classes";

const AWS = require('aws-sdk');

const ddb = new AWS.DynamoDB.DocumentClient();
const eventDao = new EvendtDao();
const queueService = new QueueService();

export class ApiService extends BaseService {
    async processApiRequest(event: any, route: Route) {
        this.validateParam(route, 'companyId');
        this.validateParam(route, 'event');
        this.validateParam(route, 'applicationId');
        this.validateParam(route, 'token');
d
        let companyAppEvents:any = await eventDao.getCompanyAppEvents(route.queryParameters.companyId, route.queryParameters.event);
        
        if(companyAppEvents.length == 0)
            this.logToSlack('devops-event-bus', 'Eventbus Router', "Received external event for a company that doesn't have event configured."
            + " Event (" + route.queryParameters.event + ")" + ' Company ID (' + route.queryParameters.companyId + ')');

        for(let appEvent of companyAppEvents){
            if(appEvent.applicationId == route.queryParameters.applicationId)
                this.handleRequest(event, appEvent, route);
        }
    }

    async handleRequest(event: any, appEvent: CompanyAppEvent, route: Route) {
        console.log('test', appEvent.authToken)
        if(appEvent.authToken != route.queryParameters.token)
            throw new HttpError(400, 'Forbidden');
        
            console.log('test')
        let eventRequest:EventRequest = new EventRequest();
        eventRequest.applicationId = route.queryParameters.applicationId;
        eventRequest.companyAppEventId = appEvent.id;
        eventRequest.companyId = route.queryParameters.companyId;
        eventRequest.event = route.queryParameters.event;
        eventRequest.payload = event.body;

        let app = await eventDao.getApplication(route.queryParameters.applicationId);
        queueService.routeRequestToApp(eventRequest, appEvent, <Application>app);
    }

}

