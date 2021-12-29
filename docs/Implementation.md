# Reference Implementation

The high level architecture described was implemented in a real scenario with 40 sessions, where data was collected from different teams.

The implementation diagrama describes how each componente of the architecture was used to automatically generate the MMLA Interfaces.

![Image](images/Implementation.png)

## Observation tool:

A, K and Observations was developed using a nodejs Express framework and Angular as front end. The source code for this repository can be found [here](https://github.com/Teamwork-Analytics/obs-rules).

## Multimodal data collection

The multimodal data collection for our implementation consist of different applications, one per modality. The Reference Implementation presented here will explain how the indoor positioning data was collected and store. In [this repository](https://github.com/Teamwork-Analytics/MultimodalData) you will find all the scripts that we use for the reference implementation.

- Data Collection/Storage (scripts)

A. Java script
B. Python script

- Data Pre-procesing

A. Python implementation

The starting point of the application is [ProximityLocalisation.py](https://github.com/Teamwork-Analytics/obs-rules/blob/gloriaRules/server/routes/localisation/ProximityLocalisation.py). This scripts reads the raw data as a dataFrame in Python.

Once the application reads the data, the formating is mandatory. The python script [formatingDataSetProximity.py](https://github.com/Teamwork-Analytics/obs-rules/blob/gloriaRules/server/routes/localisation/formatingDataSetProximity.py) is the first formating process that we run on the raw positioning data. That way, we waranty that the attributes trackerId,x,y,rotation,sessionId are in the right format and normalised.

Please keep in touch if you have intered in developing or contributing to create other applications for other modalities.

## Multimodal modelling

- A set of techniques was develop to automatically generate bar charts, ego-networks and full-social networks. In the server side of the Observation tool the Indoor Positioning app was configured to read the positioning data as well as to automatically analysi different Personal proxemics. Please find the source code [here](https://github.com/Teamwork-Analytics/obs-rules/tree/gloriaRules/server/routes/localisation).

The scripts used fot the multimodal modelling are:

1. The python script [enumerateTrackersProximity.py](https://github.com/Teamwork-Analytics/obs-rules/blob/gloriaRules/server/routes/localisation/enumerateTrackersProximity.py) is used to contextualise the data and change the trackerId by the role that students performed during the simulation.
2. The python script [distancesProximity.py](https://github.com/Teamwork-Analytics/obs-rules/blob/gloriaRules/server/routes/localisation/distancesProximity.py) is in charge of validating second per second how far nurses apart from each other. That way the interpersonal proxemic can be calculated and included into a matrix. Interpersona distances can be: intimate, personal, social and public.
3. Then the script named [visualisationProximity.py](https://github.com/Teamwork-Analytics/obs-rules/blob/gloriaRules/server/routes/localisation/visualisationProximity.py) visualise the outcomes in the form of a bar chart, a ego-network or a full-graph network. This scripts also generates specific narratives for the bar charts, which is going to be included as part of the visualisation. 

## Data Visualisation

To visualise the outcomes from the multimodal modelling process we are using two different technologies:
vis.js and angular.

## Layered Storytelling


