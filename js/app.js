import Amplify from '@aws-amplify/core';

// AWS Amplify configuration as a global
const amplify = {
    cognito: {
        region: 'us-east-1',
        userPoolId: 'us-east-1_u8wFFnlzK',
        userPoolWebClientId: '4ecml735e17g8b0q6mrpedejfv'
    }
}

// Initialize AngularJS module for SPA
angular.module('Rutastic', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            // Default route mapped to the landing page
            .when('/', {
                controller: '',
                controllerAs: '',
                templateUrl: 'pages/landingPage.html',
            })
            .when('/Registro', {
                controller: 'registrationController',
                controllerAs: 'registrationVM',
                templateUrl: 'pages/registration.html'
            })
            .when('/Verificar/:username?', {
                controller: 'accountVerificationController',
                controllerAs: 'verificationVM',
                templateUrl: 'pages/verify.html'
            })
            .when('/Login/:username?', {
                controller: 'loginController',
                controllerAs: 'loginVM',
                templateUrl: 'pages/login.html'
            })
            .when('/usuarios/EditarPerfil/:username', {
                controller: 'userController',
                controllerAs: 'userVM',
                templateUrl: 'pages/editProfile.html'
            })
            .when('/rutas/CrearRuta', {
                controller: 'routeHandlerController',
                controllerAs: 'routeHandlerVM',
                templateUrl: 'pages/routeCRUD.html'
            })
            .when('/rutas/EditarRuta/:ID', {
                controller: 'routeHandlerController',
                controllerAs: 'routeHandlerVM',
                templateUrl: 'pages/routeCRUD.html'
            })
            .when('/rutas/FiltrarRutas', {
                controller: 'routeQueryController',
                controllerAs: 'routeQueryVM',
                templateUrl: 'pages/queryRoutes.html'
            })
            .when('/rutas/DetallesRuta/:ID', {
                controller: 'routeDetailsController',
                controllerAs: 'routeDetailsVM',
                templateUrl: 'pages/routeDetails.html'
            })
        ;
    });

// Initialize Amplify library for authentication with existing Cognito User Pool
Amplify.configure({
    Auth: {
        // Amazon Cognito Region
        region: amplify.cognito.region,

        // Amazon Cognito User Pool ID
        userPoolId: amplify.cognito.userPoolId,

        // Amazon Cognito Web Client ID
        userPoolWebClientId: amplify.cognito.userPoolWebClientId
    }
});