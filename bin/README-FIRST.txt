KINDR COMPLETE INTEGRATED PROJECT
=================================

One Spring Boot app on port 8080:
- Register / Login / Session
- Forgot password / OTP email / Reset password
- Contact backend
- Campaign
- Payment + Razorpay
- Donation history / dashboard support used by the existing frontend

FRONTEND
--------
The browser frontend files were copied unchanged into src/main/resources/static.
Open: http://localhost:8080/
Do not run the old Node server.js for this integrated build, because it contains a second local API implementation.

DATABASE
--------
Default: kindr_db
Default MySQL user/password: root/root
Override with DB_URL, DB_USERNAME, DB_PASSWORD.

RAZORPAY
--------
Set:
RAZORPAY_KEY_ID=...
RAZORPAY_KEY_SECRET=...
If not set, the existing checkout frontend uses the backend test-payment mode.

FORGOT PASSWORD EMAIL
---------------------
Set:
MAIL_USERNAME=your Gmail address
MAIL_APP_PASSWORD=your Gmail app password

FRONTEND REGISTRATION CONTRACT
------------------------------
The existing frontend sends:
firstName, lastName, email, phone, accountType, password, confirmPassword
Account types accepted: Fundraiser, Donor, Organization
Java field phone maps to existing DB column mobile_number.
BCrypt password maps to existing DB column password.

CONTACT PAGE NOTE
-----------------
The Contact backend is integrated at POST /api/contact.
The supplied existing contact-us.html contains no API-submit JavaScript and no named form fields.
Because the requirement was to leave frontend code unchanged, that page itself is intentionally unchanged.
To make its Send Message button persist data, a small frontend wiring change would be required later.
