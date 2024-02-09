## Require:

JDK 17 and IntelliJ Ulitmate (It is better to use the services tab in IntelliJ which is in View-> Tool Windows -> Services).

## Quick Setup:

When you load this project into IntelliJ, you'll encounter a prompt indicating `Maven Build script found`. Be sure to select the `Load` button to initialize the Maven project in IntelliJ.

If you forget to "Load" the Maven Build script for the yarn-sense Project: It is recommended to `File | Close Project` and remove the project from the recent projects. Once removed from the recent projects list, please ensure to delete the `.idea` folder before loading the project

Next, follow these steps to swiftly get started:
1. Open the terminal within your preferred IDE, ensuring that you're located within the `analytic` folder directory of the project.

2. Run the following command:
   `mvn clean install -U`

Once the previous steps have been excauted - The Spring Boot Services will be available under the `Services` window within your IntelliJ IDE.
To maunally enable the services window you can do this by Selecting: `View | Tool Windows | Services` from the main menu or using the shortcut `Alt + 8`.

Please note: Should you encounter any issues, it is advisable to utilize IntelliJ Ultimate Edition.

Side Note: After running `mvn clean install -U` in the project terminal, halt the application and initiate the services via the `Services` window mentioned above. Right click on `Spring Boot` and select `Run` to start all four services simultaneously.

## Frontend project:

Gloria's repository:https://github.com/gloriafer18/obs-rules/tree/gloriaRules - No longer supported.

## Service running order:

1). eureka Server
   2). video, biodata(empatica), position services

## The API list:

   1. eureka server: http://localhost:8761/eureka/
      
   
   2. biodata service: http://localhost:7301			
      start recording: http://localhost:7301/bio/start/{sessionid}			
      stop recording: http://localhost:7301/bio/stop			
      device id:			
      0x6a18	27160			
      0x6a5a	27226			
      0x6a7d	27261			
      0x6a7e 	27262		
      0x6a7f	27263
   
   
   3. video service: http://localhost:7201			
      start recording: http://localhost:7101/video/start/{sessionid}			
      stop recording: http://localhost:7101/video/stop
   

   4. position service: http://localhost:7101			
      start recording: http://localhost:7201/pos/start/{sessionid}			
      stop recording: http://localhost:7201/pos/stop			
   
      device id: 	
      
      379efd (9557)			
      3b9efd (10668)			
      a39bfd (10742)


   5. audio service: http://localhost:7501			
      start : http://localhost:7501/audio/start/{seesionid}			
      stop: http://localhost:7501/audio/stop			


   6. each modality has one topic

      position topic			
      empatica topic			

      kafka data format: {"key":"key-sessionid-uuid", "value":"message"}
      position data will be saved to one sessionid.json file
