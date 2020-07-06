# PayPal Android SDK 

[![Build Status](https://travis-ci.com/paypal/android-sdk.svg?branch=master)](https://travis-ci.com/paypal/android-sdk)

## Note
This repository is currently in development. The PayPal Android SDK is not meant for public use. The repo is public for pilot merchants only.

## About
PayPal Android provides a native SDK offering to current PayPal partners. It allows existing PayPal partners to use Apple Pay and to get the benefits of Three D Secure verification. This module provides an “unbranded” checkout experience to merchants already integrated with PayPal. The prerequisite for using this module is that the merchant already has an existing PayPal server integration using Orders V2. Their server must be able to generate a PayPal client auth token and complete transactions using the PayPal backend.

## Running the Demo App

This app depends on local development versions of both braintree-android and browser-switch-android to work.

To run the app:
1. Open a new terminal tab and cd to braintree-android project
2. Checkout `add-card-tokenize-overload` branch
3. Run `./script/start-local-development.sh` from the project root directory
4. Open android-sdk PPCP project in Android studio

The start-local-development scripts both start a gradle build that continuously watches the source of its respective project, rebuilds and deploys to local maven automatically.

To build against these new artifacts when changes occur, select File -> Sync Project with Gradle Files from the menu bar in Android studio.

This will need to be done in the dependent project only, e.g. when making a change to braintree-android, Sync with Gradle Files from the android-sdk PPCP project. When making changes to browser-switch-android, do the Gradle sync from the braintre-android project, and then from the android-sdk PPCP project.


* TODO

## Android Studio 
* TODO

