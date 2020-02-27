call mvn compile package -Dmaven.test.skip=true

IF %ERRORLEVEL% NEQ 0 ( 
   EXIT /B 
)

serverless deploy --stage prod --aws-profile DigiCoreProd