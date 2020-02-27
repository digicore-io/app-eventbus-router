export class Table {
  tableName: string;
  destinationType: DestinationType;
  destinationUrl: string;
}

export const enum DestinationType {
  SQS = "SQS"
}
