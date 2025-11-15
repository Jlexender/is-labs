<template>
  <div class="import-container">
    <h2>Import Tickets</h2>
    <div class="import-form">
      <div class="file-input-wrapper">
        <label for="file-input" class="file-label">
          Choose JSON File
        </label>
        <input
          id="file-input"
          type="file"
          accept=".json"
          @change="handleFileSelect"
          class="file-input"
        />
        <span v-if="selectedFile" class="file-name">{{ selectedFile.name }}</span>
      </div>

      <div class="user-input">
        <label for="userId">User ID:</label>
        <input
          id="userId"
          v-model="userId"
          type="text"
          placeholder="Enter your user ID"
          required
        />
      </div>

      <button 
        @click="importTickets" 
        :disabled="!selectedFile || !userId || isImporting"
        class="import-button"
      >
        {{ isImporting ? 'Importing...' : 'Import Tickets' }}
      </button>

      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div>
    </div>

    <div class="format-info">
      <h3>JSON Format Example</h3>
      <pre>{{ formatExample }}</pre>
      <h3>Validation Rules</h3>
      <pre>{{ validationNotes }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedFile = ref(null);
const userId = ref('');
const isImporting = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

const formatExample = `[
  {
    "name": "Concert Ticket",
    "coordinates": { "x": 10.5, "y": 20 },
    "price": 100.0,
    "discount": 10,
    "number": 12345,
    "type": "VIP",
    "comment": "Great seats",
    "person": {
      "eyeColor": "BLUE",
      "hairColor": "RED",
      "nationality": "JAPAN",
      "birthday": "1990-01-01T00:00:00.000+00:00",
      "location": {
        "x": 1,
        "y": 2,
        "z": 3,
        "locationName": "Tokyo"
      }
    },
    "event": {
      "eventName": "Rock Concert",
      "eventType": "FOOTBALL",
      "ticketsCount": 1000
    },
    "venue": {
      "venueName": "Stadium",
      "capacity": 50000,
      "address": {
        "street": "Main St",
        "town": "Downtown"
      }
    }
  }
]`;

const validationNotes = `Field constraints:
- name: required, non-empty string
- coordinates.x: max 397
- coordinates.y: required, max 315
- price: required, positive
- discount: required, 1-100
- number: required, positive, unique
- type: VIP | BUDGETARY | CHEAP (optional)
- person.eyeColor: GREEN | RED | BLUE | YELLOW (optional)
- person.hairColor: GREEN | RED | BLUE (optional)
- person.nationality: INDIA | THAILAND | SOUTH_KOREA | JAPAN (optional)
- event.eventType: FOOTBALL | BASEBALL | THEATRE_PERFORMANCE | EXPOSITION (required if event present)
- venue.address: required if venue present`;

function handleFileSelect(event) {
  selectedFile.value = event.target.files[0];
  errorMessage.value = '';
  successMessage.value = '';
}

async function importTickets() {
  if (!selectedFile.value || !userId.value) {
    errorMessage.value = 'Please select a file and enter user ID';
    return;
  }

  errorMessage.value = '';
  successMessage.value = '';
  isImporting.value = true;

  try {
    const fileContent = await selectedFile.value.text();
    const tickets = JSON.parse(fileContent);

    if (!Array.isArray(tickets)) {
      throw new Error('File must contain an array of tickets');
    }

    const response = await fetch(
      `http://${import.meta.env.VITE_BACKEND_SOCKET}/api/ticket/import?userId=${encodeURIComponent(userId.value)}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tickets)
      }
    );

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Import failed');
    }

    const result = await response.json();
    
    if (result.status === 'FAILED') {
      throw new Error(result.errorMessage || 'Import failed on server');
    }
    
    successMessage.value = `Successfully imported ${result.objectCount} tickets!`;
    selectedFile.value = null;
    document.getElementById('file-input').value = '';
  } catch (error) {
    // Handle JSON parse errors
    if (error instanceof SyntaxError) {
      errorMessage.value = 'Invalid JSON format. Please check your file.';
    } else {
      errorMessage.value = error.message || 'Failed to import tickets';
    }
  } finally {
    isImporting.value = false;
  }
}
</script>

<style scoped>
.import-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  color: #93c5fd; /* blue-300 */
  margin-bottom: 20px;
  font-size: 1.25rem;
}

.import-form {
  background: #111827; /* gray-900 */
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
}

.file-input-wrapper {
  margin-bottom: 15px;
}

.file-label {
  display: inline-block;
  padding: 10px 20px;
  background: #2563eb; /* blue-600 */
  color: #f9fafb; /* gray-50 */
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}

.file-label:hover {
  background: #1d4ed8; /* blue-700 */
}

.file-input {
  display: none;
}

.file-name {
  margin-left: 10px;
  color: #9ca3af; /* gray-400 */
}

.user-input {
  margin-bottom: 15px;
}

.user-input label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #d1d5db; /* gray-300 */
}

.user-input input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 6px;
  font-size: 14px;
  background: #0f172a; /* slate-900 */
  color: #e5e7eb; /* gray-200 */
}

.user-input input::placeholder {
  color: #6b7280; /* gray-500 */
}

.user-input input:focus {
  outline: none;
  border-color: #2563eb; /* blue-600 */
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.25);
}

.import-button {
  width: 100%;
  padding: 12px;
  background: #2563eb; /* blue-600 */
  color: #f9fafb; /* gray-50 */
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.import-button:hover:not(:disabled) {
  background: #1d4ed8; /* blue-700 */
}

.import-button:disabled {
  background: #374151; /* gray-700 */
  cursor: not-allowed;
  opacity: 0.5;
}

.error-message {
  margin-top: 15px;
  padding: 10px;
  background: #7f1d1d; /* red-900 */
  border: 1px solid #991b1b; /* red-800 */
  border-radius: 6px;
  color: #fca5a5; /* red-300 */
}

.success-message {
  margin-top: 15px;
  padding: 10px;
  background: #14532d; /* green-900 */
  border: 1px solid #166534; /* green-800 */
  border-radius: 6px;
  color: #86efac; /* green-300 */
}

.format-info {
  background: #111827; /* gray-900 */
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
}

.format-info h3 {
  color: #93c5fd; /* blue-300 */
  margin-bottom: 10px;
  font-size: 1rem;
}

.format-info pre {
  background: #0b1220; /* deep dark */
  border: 1px solid #1f2937; /* gray-800 */
  padding: 15px;
  border-radius: 6px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.4;
  color: #d1d5db; /* gray-300 */
}
</style>
