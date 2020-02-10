import { EvendtDao } from "./dao/event-dao";
const uuidv4 = require("uuid/v4");
const AWS = require("aws-sdk");
const axios = require("axios");
const eventDao = new EvendtDao();

export class SharpSpringService {
  async processMessage(item: any) {
    let eventRequest: EventRequest = <EventRequest>JSON.parse(item.body);
    let companyAppEvent = <CompanyAppEvent>await eventDao.getCompanyAppEvent(eventRequest.companyAppEventId);
    let config: any = JSON.parse(companyAppEvent.config);
    let url: string = "https://api.sharpspring.com/pubapi/v1.2?accountID=" + config.accountId + "&secretKey=" + config.secretKey;

    let result: any = await this.createContact(eventRequest, url);

    if (result.error.length > 0 && result.error[0].code == 301) {
      if (eventRequest.payload.allowCustomerUpdate) {
        console.log("Updating contact");
        result = await this.updateContact(eventRequest, url);
      } else console.log("Customer already exists but updates were not requested");
    }

    console.log("RESULT", result);
  }

  async updateContact(eventRequest: EventRequest, url) {
    return new Promise(async function(resolve, reject) {
      let helper = new Helper();
      let request = helper.createRequest(eventRequest, "updateLeads");

      console.log(JSON.stringify(request));
      let result = await axios.post(url, request);

      resolve(result.data);
    });
  }

  async createContact(eventRequest: EventRequest, url: string) {
    return new Promise(async function(resolve, reject) {
      let helper = new Helper();
      let request = helper.createRequest(eventRequest, "createLeads");

      console.log(JSON.stringify(request));
      let result = await axios.post(url, request);

      resolve(result.data);
    });
  }
}

export class Helper {
  createRequest(eventRequest, method) {
    let request: any = {};
    request.id = uuidv4();
    request.method = method;
    request.params = {};
    request.params.objects = [];

    let contact = eventRequest.payload.customer;

    request.params.objects.push(contact);

    return request;
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
