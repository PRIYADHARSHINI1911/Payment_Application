# Payment Application UI Dashboard

A modern, responsive web dashboard for interacting with the Payment Application backend APIs.

## Overview

This dashboard provides a user-friendly interface to:
- **Create Payments**: Submit payment requests with customer details and card information
- **Register Webhooks**: Register webhook URLs to receive payment event notifications
- **Monitor API Activity**: Real-time request/response logging with detailed API interaction history

## Features

✨ **Modern UI Design**
- Clean, intuitive interface with a gradient header
- Responsive layout that works on desktop and mobile devices
- Real-time request/response logging

🔐 **Payment Management**
- Form validation for all required fields
- Support for payment amounts with decimal precision
- Card number formatting for better usability

🔗 **Webhook Registration**
- Easy webhook URL registration
- Client ID based organization
- Automatic URL auto-complete for testing

📊 **Development Tools**
- Real-time API request/response logger
- Color-coded request/response display
- Detailed error messages and status codes
- Easy log clearing for fresh starts

## Files Structure

```
src/main/resources/static/
├── index.html       # Main dashboard HTML
├── styles.css       # Responsive styling and theme
└── script.js        # JavaScript for API interactions and form handling

src/main/java/com/application/payment/config/
└── CorsConfig.java  # CORS configuration for browser requests
```

## How to Use

### 1. Start the Application

```bash
cd payment
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### 2. Access the Dashboard

Open your browser and navigate to:
```
http://localhost:8080
```

### 3. Create a Payment

1. Fill in the payment form:
   - **Client ID**: A unique identifier for your client (e.g., `client_123`)
   - **First Name**: Customer's first name (e.g., `John`)
   - **Last Name**: Customer's last name (e.g., `Doe`)
   - **ZIP Code**: Customer's postal code (e.g., `12345`)
   - **Amount**: Payment amount (e.g., `100.50`)
   - **Card Number**: Credit card number (test: `4111111111111111`)

2. Click **"Create Payment"** button

3. Check the response in the **Response** section below the form

### 4. Register a Webhook

1. Fill in the webhook form:
   - **Client ID**: Same client ID used for payments
   - **Webhook URL**: URL where payment events will be sent (e.g., `http://localhost:8080/mockWebhook/receive`)

2. Click **"Register Webhook"** button

3. The response will show confirmation of webhook registration

### 5. Monitor API Activity

All API requests and responses are logged in the **API Request/Response Log** section at the bottom:
- **Green border**: Successful response (2xx status)
- **Red border**: Error or failed response
- **Timestamp**: Exact time of the request
- **Payload**: Full request/response JSON

Click **"Clear"** button to clear the log.

## API Endpoints

The dashboard communicates with these backend endpoints:

### Create Payment
```
POST /v1/createPayment
Content-Type: application/json

{
  "clientId": "client_123",
  "firstName": "John",
  "lastName": "Doe",
  "zip": "12345",
  "cardNumber": "4111111111111111",
  "amount": 100.50
}

Response (Success - 200):
{
  "message": "Payment created successfully!"
}

Response (Error - 400/500):
{
  "message": "Error description"
}
```

### Register Webhook
```
POST /v1/registerWebhook
Content-Type: application/json

{
  "clientId": "client_123",
  "url": "http://localhost:8080/mockWebhook/receive"
}

Response (Success - 200):
{
  "message": "Webhook registered successfully!"
}

Response (Error - 400/500):
{
  "message": "Error description"
}
```

## Configuration

### CORS Settings
The application is configured to accept requests from:
- `http://localhost:8080`
- `http://127.0.0.1:8080`
- `http://localhost:3000` (for future frontend frameworks)
- `http://127.0.0.1:3000`

To add more origins, modify `CorsConfig.java`

### API Base URL
The default API base URL is set to `http://localhost:8080`

To change it, edit `script.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080';
```

## Testing

### Test Data

**Test Credit Card:**
- Card Number: `4111111111111111`
- Amount: `100.50`
- Client ID: `client_123`

**Test Webhook URL:**
- `http://localhost:8080/mockWebhook/receive`

### Manual Testing Steps

1. Start the Payment Application
2. Open the dashboard at `http://localhost:8080`
3. Register a webhook for your test client:
   - Client ID: `test_client_001`
   - Webhook URL: `http://localhost:8080/mockWebhook/receive`
4. Create a test payment:
   - Client ID: `test_client_001`
   - First Name: `Test`
   - Last Name: `User`
   - ZIP: `00000`
   - Amount: `50.00`
   - Card: `4111111111111111`
5. Check the webhook logs for payment events

## Browser Compatibility

- Chrome/Edge 88+
- Firefox 87+
- Safari 14+
- Any modern browser with ES6 support

## Troubleshooting

### Issue: "Failed to connect to API"
- Ensure the backend service is running on `http://localhost:8080`
- Check that the API base URL in `script.js` is correct
- Verify CORS is properly configured

### Issue: Payment fails with "Webhook URL is empty"
- Register a webhook for the client ID first before creating payments
- Ensure the webhook URL is properly formatted

### Issue: Request not appearing in log
- Check browser console (F12) for JavaScript errors
- Verify network tab to see actual request/response
- Check server logs for backend errors

## Development

### Adding New Features

1. **New API Endpoints**: Add to `ENDPOINTS` object in `script.js`
2. **New Form Fields**: Add inputs to `index.html` and update `script.js` form handlers
3. **Styling Changes**: Modify `styles.css` using CSS custom properties (variables)
4. **Theme Customization**: Edit `:root` CSS variables for colors and spacing

### Example: Adding a new endpoint

```javascript
// In script.js, add to ENDPOINTS:
const ENDPOINTS = {
    CREATE_PAYMENT: '/v1/createPayment',
    REGISTER_WEBHOOK: '/v1/registerWebhook',
    GET_PAYMENTS: '/v1/payments'  // New endpoint
};

// Create handler function:
async function handleGetPayments() {
    // Implementation
}
```

## Security Notes

⚠️ **Important**: This dashboard is designed for development/testing only.

- Card numbers are sent in plain text (in production, use encryption)
- No authentication is enforced
- CORS is open to localhost

For production:
- Implement proper authentication/authorization
- Use HTTPS for all communications
- Validate and sanitize all inputs on both client and server
- Implement rate limiting
- Never store sensitive data in localStorage
- Use proper security headers

## Related Files

- Backend Controllers: `src/main/java/com/application/payment/controller/`
- API Models: `src/main/java/com/application/payment/model/`
- OpenAPI Spec: `src/main/resources/static/openapi.yaml`
- Test Artifacts: `Testing_Artifacts/` directory

## Support

For issues or questions:
1. Check the browser console (F12) for error messages
2. Review the server logs in terminal
3. Verify all form inputs are correctly filled
4. Check the API Request/Response Log in the dashboard

## License

Part of the Payment Application project.
