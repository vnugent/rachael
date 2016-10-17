Rachael
===

Rachael is an IRC bot to help you deploy instant applications to OpenShift/Kubernetes.

Instant apps are those that require zero customization during deployment.

Deployment
===
0. Optional step: create a new project to house the bot app
1. Upload deployment template to your project

 ```
 oc create -f openshift/rachael-template.yaml
 ```
2. Use OpenShift UI to prepare the deployment.  Specify IRC server name, channels,  API endpoint, etc. as appropriate for your environment.

3. Send your request as a private message to the bot

OpenShift command references
===

The bot supports limited `oc` syntax.  By default your IRC nick is used as the target project for the deployment.  To deploy to a different project specify `--namespace=<your project>`

### Create a new app from template

 ```
 oc new-app --template=<template name> [--namespace=<project>]
  ```
### Redeploy an app

 Redploy an app previously deployed with `new-app`
 ```
 oc deploy <DeploymentConfig name> [--latest] [--namespace=<project>]
 ```
 
### List all deployed apps
 
 ```
 oc list-app [--namespace=<project>]
 ```
 
 ### Show more details about an app
 ```
 oc get-app <DeploymentConfig> [--namespace=<project>]
 ```
 
Security
===
 At the moment you must grant rest api user admin privilege to your project.

License
===
 Apache version 2.0
