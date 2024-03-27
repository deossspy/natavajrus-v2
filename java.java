// (Receive authCode via HTTPS POST)


if (request.getHeader("X-Requested-With") == null) {
  // Without the `X-Requested-With` header, this request could be forged. Aborts.
}

// Set path to the Web application client_secret_*.json file you downloaded from the
// Google API Console: https://console.cloud.google.com/apis/credentials
// You can also find your Web application client ID and client secret from the
// console and specify them directly when you create the GoogleAuthorizationCodeTokenRequest
// object.
String CLIENT_SECRET_FILE = "/path/client_secret.json";

// Exchange auth code for access token
GoogleClientSecrets clientSecrets =
    GoogleClientSecrets.load(
        JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));
GoogleTokenResponse tokenResponse =
          new GoogleAuthorizationCodeTokenRequest(
              new NetHttpTransport(),
              JacksonFactory.getDefaultInstance(),
              "https://oauth2.googleapis.com/token",
              clientSecrets.getDetails().getClientId(),
              clientSecrets.getDetails().getClientSecret(),
              authCode,
              REDIRECT_URI)  // Specify the same redirect URI that you use with your web
                             // app. If you don't have a web version of your app, you can
                             // specify an empty string.
              .execute();

String accessToken = tokenResponse.getAccessToken();

// Use access token to call API
GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
Drive drive =
    new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
        .setApplicationName("Auth Code Exchange Demo")
        .build();
File file = drive.files().get("appfolder").execute();

// Get profile info from ID token
GoogleIdToken idToken = tokenResponse.parseIdToken();
GoogleIdToken.Payload payload = idToken.getPayload();
String userId = payload.getSubject();  // Use this value as a key to identify a user.
String email = payload.getEmail();
boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
String name = (String) payload.get("name");
String pictureUrl = (String) payload.get("picture");
String locale = (String) payload.get("locale");
String familyName = (String) payload.get("family_name");
String givenName = (String) payload.get("given_name");

@RestController
public class GitHubAuthController {

    @GetMapping("/login/github")
    public String redirectToGitHubLogin(HttpServletResponse response) throws IOException {
        String githubAuthUrl = "https://github.com/login/oauth/authorize" +
                               "?client_id=516056401304-vk2dmd8l4vdc9s6rve882ec7iklq5olq.apps.googleusercontent.com" +
                               "&redirect_uri=deossspy.github.io/natavajrus-v2/login" +
                               "&scope=user";
        response.sendRedirect(githubAuthUrl);
        return null;
    }

    @GetMapping("/login/github/callback")
    public String handleGitHubCallback(@RequestParam("code") String code) {
        // Exchange code for access token
        // Fetch user information from GitHub API
        // Create user session
        // Redirect user to the homepage or dashboard
        return "redirect:/";
    }
}
