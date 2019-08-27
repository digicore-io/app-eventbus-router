
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

export class HttpError{
    status:number;
    message:string;
    constructor(status:number, message:string){
      this.status = status;
      this.message = message;
    }
  }
  
  export class Response{
    server:Server;
    status:number;
    success:boolean;
    payload:any;
    
    constructor(status:number, success:boolean, payload:any, event:any, executeTime:number){
      this.server = new Server("", new Date().getTime(), event,executeTime);
      if(success && !payload)
        this.status = 204; //No content
      else
        this.status = status;
      
      this.success = success;
      this.payload = payload;
  
    }
  }
  
  export class Server{
    api:string;
    timestamp:number;
    endpoint:string;
    environment:string;
    executeMs:number;
  
    constructor(api:string, timestamp:number, event:any, executeTime:number){
      this.api = api;
      this.timestamp = timestamp;
      this.endpoint = event.path;
      this.executeMs = executeTime;
    }
  }

  export class Route {
    method: string;
    resource: string;
    pathParameters: any;
    queryParameters: any;
  
    constructor(method:string, resource:string, pathParameters:any, queryParameters:any){
      this.method = method;
      this.resource = resource;
      this.pathParameters = pathParameters;
      this.queryParameters = queryParameters;
    }
  }
  