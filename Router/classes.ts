
export class EventRequest {
    companyId: string;
    event: string;
    applicationId: string;
    payload: any;
    response: any;
    companyAppEventId:string;
}

export class CompanyAppEvent {
    id: string;
    companyId: string;
    event: string;
    applicationId: string;
    config: any;
    authToken:string;
}

export class Application {
    id: string;
    destinationType: DestinationType;
    destinationUrl: string;
    destinationArn: string;
}

export const enum DestinationType {
    SQS = 'SQS',
}


  
  

  
  