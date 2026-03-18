// Configuration
const API_BASE_URL = 'http://localhost:8080';
const ENDPOINTS = {
    CREATE_PAYMENT: '/api/payments',
    REGISTER_WEBHOOK: '/api/webhooks',
    GET_HISTORY: '/api/history'
};

// DOM Elements
const paymentForm = document.getElementById('paymentForm');
const webhookForm = document.getElementById('webhookForm');
const paymentResponse = document.getElementById('paymentResponse');
const webhookResponse = document.getElementById('webhookResponse');
const paymentResponseContent = document.getElementById('paymentResponseContent');
const webhookResponseContent = document.getElementById('webhookResponseContent');
const logger = document.getElementById('logger');
const clearLoggerBtn = document.getElementById('clearLogger');
const refreshHistoryBtn = document.getElementById('refreshHistory');
const historyTable = document.getElementById('historyTable');

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    paymentForm.addEventListener('submit', handlePaymentSubmit);
    webhookForm.addEventListener('submit', handleWebhookSubmit);
    clearLoggerBtn.addEventListener('click', clearLogger);
    refreshHistoryBtn.addEventListener('click', fetchPaymentHistory);
});

/**
 * Handle payment form submission
 */
async function handlePaymentSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(paymentForm);
    const payload = {
        clientId: formData.get('clientId'),
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        zip: formData.get('zip'),
        cardNumber: formData.get('cardNumber'),
        amount: parseFloat(formData.get('amount'))
    };

    await sendRequest(
        ENDPOINTS.CREATE_PAYMENT,
        payload,
        paymentResponseContent,
        paymentResponse
    );
}

/**
 * Handle webhook form submission
 */
async function handleWebhookSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(webhookForm);
    const payload = {
        clientId: formData.get('clientId'),
        url: formData.get('url')
    };

    await sendRequest(
        ENDPOINTS.REGISTER_WEBHOOK,
        payload,
        webhookResponseContent,
        webhookResponse
    );
}

/**
 * Send API request
 */
async function sendRequest(endpoint, payload, responseElement, responseContainer) {
    const button = event.target.querySelector('button[type="submit"]');
    button.disabled = true;
    button.classList.add('loading');

    // Log the request
    logRequest(endpoint, payload);

    try {
        const url = `${API_BASE_URL}${endpoint}`;
        
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        // Log the response
        logResponse(endpoint, response.status, data, response.ok);

        // Display response
        displayResponse(responseElement, responseContainer, data, response.ok);

        // Reset form if successful
        if (response.ok) {
            setTimeout(() => {
                if (endpoint === ENDPOINTS.CREATE_PAYMENT) {
                    paymentForm.reset();
                } else {
                    webhookForm.reset();
                }
            }, 1000);
        }
    } catch (error) {
        // Log error
        logError(endpoint, error);

        // Display error
        const errorData = { message: `Error: ${error.message}` };
        displayResponse(responseElement, responseContainer, errorData, false);
    } finally {
        button.disabled = false;
        button.classList.remove('loading');
    }
}

/**
 * Display response in UI
 */
function displayResponse(responseElement, responseContainer, data, isSuccess) {
    responseContainer.classList.remove('hidden');
    responseElement.classList.remove('success', 'error');
    responseElement.classList.add(isSuccess ? 'success' : 'error');
    
    const formattedData = JSON.stringify(data, null, 2);
    responseElement.textContent = formattedData;

    // Scroll to response
    responseContainer.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

/**
 * Log request to logger
 */
function logRequest(endpoint, payload) {
    const entry = document.createElement('div');
    entry.className = 'log-entry request';
    
    const timestamp = new Date().toLocaleTimeString();
    const content = `
<div class="log-timestamp">${timestamp}</div>
<div class="log-method post">POST ${API_BASE_URL}${endpoint}</div>
<div class="log-content">${JSON.stringify(payload, null, 2)}</div>
    `.trim();
    
    entry.innerHTML = content;
    logger.insertBefore(entry, logger.firstChild);
}

/**
 * Log response to logger
 */
function logResponse(endpoint, status, data, isSuccess) {
    const entry = document.createElement('div');
    entry.className = `log-entry response-${isSuccess ? 'success' : 'error'}`;
    
    const timestamp = new Date().toLocaleTimeString();
    const statusColor = isSuccess ? 'color: #52c41a' : 'color: #ff4d4f';
    const content = `
<div class="log-timestamp">${timestamp}</div>
<div class="log-method" style="${statusColor}">← ${status}</div>
<div class="log-content">${JSON.stringify(data, null, 2)}</div>
    `.trim();
    
    entry.innerHTML = content;
    logger.insertBefore(entry, logger.firstChild);
}

/**
 * Log error to logger
 */
function logError(endpoint, error) {
    const entry = document.createElement('div');
    entry.className = 'log-entry response-error';
    
    const timestamp = new Date().toLocaleTimeString();
    const content = `
<div class="log-timestamp">${timestamp}</div>
<div class="log-method" style="color: #ff4d4f;">✗ ERROR</div>
<div class="log-content">Failed to connect to ${API_BASE_URL}${endpoint}
Error: ${error.message}</div>
    `.trim();
    
    entry.innerHTML = content;
    logger.insertBefore(entry, logger.firstChild);
}

/**
 * Clear logger
 */
function clearLogger() {
    logger.innerHTML = '';
    const message = document.createElement('div');
    message.className = 'log-entry';
    message.textContent = 'Logger cleared. Make requests to see them here.';
    message.style.color = 'var(--text-secondary)';
    logger.appendChild(message);
}

/**
 * Format card number input
 */
document.getElementById('cardNumber').addEventListener('input', (e) => {
    let value = e.target.value.replace(/\s+/g, '');
    let formatted = value.match(/.{1,4}/g)?.join(' ') || value;
    e.target.value = formatted;
});

/**
 * Auto-fill webhook URL example
 */
document.getElementById('webhookUrl').addEventListener('focus', (e) => {
    if (!e.target.value) {
        e.target.value = 'http://localhost:8080/mockWebhook/receive';
    }
});

// Log initial ready state
window.addEventListener('load', () => {
    const readyEntry = document.createElement('div');
    readyEntry.className = 'log-entry';
    readyEntry.innerHTML = `
<div class="log-timestamp">${new Date().toLocaleTimeString()}</div>
<div class="log-method" style="color: #52c41a;">✓ Dashboard Ready</div>
<div class="log-content">Connected to API at ${API_BASE_URL}</div>
    `.trim();
    logger.appendChild(readyEntry);
});

/**
 * Fetch payment history from database
 */
async function fetchPaymentHistory() {
    historyTable.innerHTML = '<p class="history-loading">Loading payment history...</p>';
    
    try {
        const url = `${API_BASE_URL}${ENDPOINTS.GET_HISTORY}`;
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const payments = await response.json();
        
        // Log the fetch
        logRequest('GET', ENDPOINTS.GET_HISTORY);
        logResponse(ENDPOINTS.GET_HISTORY, response.status, { count: payments.length }, true);

        // Display the table
        displayPaymentHistory(payments);
    } catch (error) {
        console.error('Error fetching history:', error);
        logError(ENDPOINTS.GET_HISTORY, error);
        historyTable.innerHTML = `<div class="history-empty"><p>❌ Failed to load payment history</p><p>${error.message}</p></div>`;
    }
}

/**
 * Display payment history in a table
 */
function displayPaymentHistory(payments) {
    if (payments.length === 0) {
        historyTable.innerHTML = '<div class="history-empty"><p>📭 No payment history found</p></div>';
        return;
    }

    let html = `
        <table class="history-table">
            <thead>
                <tr>
                    <th>Payment ID</th>
                    <th>Client ID</th>
                    <th>Name</th>
                    <th>Amount</th>
                    <th>Card Number</th>
                    <th>ZIP</th>
                    <th>Timestamp</th>
                </tr>
            </thead>
            <tbody>
    `;

    payments.forEach(payment => {
        const timestamp = payment.transactionTime 
            ? new Date(payment.transactionTime).toLocaleString() 
            : 'N/A';
        
        const amount = payment.amount 
            ? `$${parseFloat(payment.amount).toFixed(2)}`
            : 'N/A';

        html += `
            <tr>
                <td>${payment.paymentId || '-'}</td>
                <td>${payment.clientId || '-'}</td>
                <td>${payment.firstName} ${payment.lastName}</td>
                <td class="currency">${amount}</td>
                <td class="card-number">${payment.encryptedCardNumber ? '••••' + payment.encryptedCardNumber.slice(-4) : '-'}</td>
                <td>${payment.zip || '-'}</td>
                <td class="timestamp">${timestamp}</td>
            </tr>
        `;
    });

    html += `
            </tbody>
        </table>
    `;

    historyTable.innerHTML = html;
}

/**
 * Log request (updated for GET requests)
 */
function logRequest(method, endpoint) {
    const entry = document.createElement('div');
    entry.className = 'log-entry request';
    
    const timestamp = new Date().toLocaleTimeString();
    const methodColor = method === 'GET' ? 'color: #1890ff' : 'color: #faad14';
    const content = `
<div class="log-timestamp">${timestamp}</div>
<div class="log-method" style="${methodColor}">${method} ${API_BASE_URL}${endpoint}</div>
<div class="log-content">Request sent</div>
    `.trim();
    
    entry.innerHTML = content;
    logger.insertBefore(entry, logger.firstChild);
}
