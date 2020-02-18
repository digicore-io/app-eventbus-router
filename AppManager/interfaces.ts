export interface AppEntity {
  applicationId: string;
  companyId: string; //HashKey
  "companyId-digicoreEntityId": string; //RangeKey
  digicoreEntityId: string;
  externalEntityId;
  data: any;
}
