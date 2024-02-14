export const oktaConfig = {
    clientId: '0oaek18rcme54Yh0X5d7',
    issuer: 'https://dev-81618419.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
}