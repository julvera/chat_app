# Android chat app by J. Vera - V. Wiart 5ETI - Info

## Points done
- Sign in
- Sign up
- Public Group Chat
- Private Chat for logged users
- Profile Editing
- Image sharing for logged users (from Gallery and Camera)
-

## Tech/features/libraries used
- Firebase
- Butterknife
- 

## Unordered wish list
- Add and Edit profile picture
- Improve private chat UI with images upload
- Hash passwords in DB
- Use RecyclerView for messages
- Use Glide for image management / Stop formatting in Base64
- Actually implement "remember me" with cookies
- Find a way to implement "forgot password" feature. maybe use email addresses for login
- Make the image uploaded clickable so the message receiver can see it bigger and save it
- Make HomePage loading quicker. Might be slow because of a dirty DB request.
- Make title in messages be the username we are chatting with and not just "ChatApp"
- Find things to do under the "settings" functionality in side menu
- Add delete account/user functionality, should delete all related conversations
- Enable guests on GuestChat to re-log in with same username (store token in local storage)
- Modify DB structure to store messages in a separate instance and make users have references in there, so we can stop data duplication
- Create a real profile with multiple preferences and settings
- Implements global notifications
- Improve UI and UX
- By default anyone logged in can chat with every registered user,
   implements contact and "friend requests", default should be no contact.
- Add timestamp to messages
