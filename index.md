
## Welcome to the yarn-sense project for Teamwork Analytics

This research project aims to provide evidence for teachers and studets to reflect upon their f2f teamwork activity. The code described in this page belongs to a multimodal learning (MMLA) architecture implemented to automatically generate MMLA user-interfaces to support reflection practices.

These are some examples of MMLA interfaces:

- Gloria Milena Fernández-Nieto, Simon Buckingham Shum, Kirsty Kitto, and Roberto Martínez-Maldonado. 2022. Beyond the LearningAnalytics Dashboard: Alternative Ways to Communicate Student Data Insights Combining Visualisation, Narrative and Storytelling.InLAK22: 12th International Learning Analytics and Knowledge Conference (LAK22), March 21–25, 2022, Online, USA.ACM, New York,NY, USA, 16 pages. [https://doi.org/10.1145/3506860.3506895](https://doi.org/10.1145/3506860.3506895)
- G. M. Fernandez-Nieto et al., "Storytelling With Learner Data: Guiding Student Reflection on Multimodal Team Data," in IEEE Transactions on Learning Technologies, doi: [10.1109/TLT.2021.3131842](10.1109/TLT.2021.3131842).
- Gloria Fernandez-Nieto, Roberto Martinez-Maldonado, Vanessa Echeverria, Kirsty Kitto, Pengcheng An,and Simon Buckingham Shum. 2021. What Can Analytics for Teamwork Proxemics Reveal About Positioning Dynamics In Clinical Simulations?.Proc. ACM Hum.-Comput. Interact.5, CSCW1, Article 185 (April 2021),24 pages. [https://doi.org/10.1145/3449284](https://doi.org/10.1145/3449284)

### Repositories

This GitHub project explain the code used to generate multimodal leaning analytic interfaces to support teamwork activity. Different data **modalities** are normally collected in an in-the-wild study. Each modality is independientelly pre-processed... Modalities _Audio_ and `Code` text

1. [Obs-tools](https://github.com/Teamwork-Analytics/obs-rules) repo: is the observation tool and UI. With this application; 1) observations can be canptured and 2) different proxemics visualisations (e.g. proxemics ego-networks and full-networks) can also be automatically generated. Modality: Epistemic_, Actions_
2. [multimodal-audio](https://github.com/Teamwork-Analytics/multimodal-audio) repo: Modality: Audio_ and Video_
3. [MultimodalData](https://github.com/Teamwork-Analytics/MultimodalData) where all the scripts used for data collection, storage and pre-processing are stored per modality.

### Architecture

The architecture for implementing the MMLA solution is described in the reference implementation in:

- In progess. 2022. YARN-Sense: A Conceptual Architecture for Automatically Generating Multimodal Data Storytelling Interfaces for Supporting Team Learning. [in progress]


A whole explanation of the architecture is presented in the paper cited above. A brief explanation of the high level architecture is presented [here ](docs/architecture.md).

Additionally, a Reference Implementation of the architecture can be consulted [here](docs/Implementation.md)

## License

All the products [code] described in this project are licended under the Creative Commons Legal Code Licence CCO 1.0 Universal. Please read the details [here](https://github.com/Teamwork-Analytics/yarn-sense/blob/main/LICENSE)

### Whant to contribute or use the tool?

We encourage to contact us to collaborate [contact support](https://support.github.com/contact) and we can validate how can we do so.
