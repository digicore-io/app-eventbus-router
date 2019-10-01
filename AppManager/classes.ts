export class Location {
  id: string;
  coordinates: Array<string>;
  created: number;
  userId: string;
  companyId: string;
  username: string;
}

export class LocationRequest {
  companyId: string;
  startTime: number;
  endTime: number;
}

export class Params {
  TableName: string;
  Item: any;
  IndexName: string;
  KeyConditionExpression: string;
  ExpressionAttributeNames: any;
  ExpressionAttributeValues: any;
  ScanIndexForward: boolean;
  Key: any;
}
