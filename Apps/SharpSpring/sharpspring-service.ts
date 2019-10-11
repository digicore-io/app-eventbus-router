import { EvendtDao } from "./dao/event-dao";
const uuidv4 = require("uuid/v4");
const AWS = require("aws-sdk");
const axios = require("axios");
const eventDao = new EvendtDao();

export class SharpSpringService {
  async processMessage(item: any) {
    let eventRequest: EventRequest = <EventRequest>JSON.parse(item.body);
    let companyAppEvent = <CompanyAppEvent>await eventDao.getCompanyAppEvent(eventRequest.companyAppEventId);
    await this.registerWithSharpSpring(eventRequest, companyAppEvent);
  }

  async registerWithSharpSpring(eventRequest: EventRequest, companyAppEvent: CompanyAppEvent) {
    return new Promise(async function(resolve, reject) {
      let request: any = {};
      request.id = uuidv4();
      request.method = "createLeads";
      request.params = {};
      request.params.objects = [];

      let config: any = JSON.parse(companyAppEvent.config);
      let lead: any = {};
      lead.emailAddress = eventRequest.payload.customer.emailAddress;

      for (let field of config.customFields) lead[field.customFieldName] = field.customFieldValue;

      request.params.objects.push(lead);
      let url: string = "https://api.sharpspring.com/pubapi/v1.2?accountID=" + config.accountId + "&secretKey=" + config.secretKey;
      console.log("test", url);
      console.log(JSON.stringify(request));
      let result = await axios.post(url, request);

      resolve(result.data);
      console.log("result", result.data);
    });
  }
}

export class EventRequest {
  companyId: string;
  event: string;
  applicationId: string;
  payload: any;
  response: any;
  companyAppEventId: string;
}

export class CompanyAppEvent {
  id: string;
  companyId: string;
  event: string;
  applicationId: string;
  config: any;
}
