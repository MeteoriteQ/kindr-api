# kindr-api

Profile photos: `PATCH /api/users/me/avatar` accepts an authenticated multipart
upload with an `image` field (JPEG/PNG, up to 5 MB and 25 megapixels).
Files are saved in `uploads/` relative to the API working directory; configure
`kindr.upload-dir` to use a different persistent directory. The public URL is
stored in `users.avatar_path` and returned as `user.avatarUrl` by the account APIs.
The existing Hibernate `ddl-auto=update` setting adds the column on API startup.
Restart the API after updating the backend. Keep the uploads directory alongside
database backups. Replaced files are retained; new uploads do not use `avatar_data`.
