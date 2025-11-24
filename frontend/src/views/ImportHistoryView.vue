<template>
  <div class="history-container">
    <h2>Import History</h2>
    
    <div class="filters">
      <div class="filter-group">
        <label for="userId">User ID:</label>
        <input
          id="userId"
          v-model="userId"
          type="text"
          placeholder="Enter your user ID"
        />
      </div>
      
      <div class="filter-group">
        <label>
          <input
            v-model="isAdmin"
            type="checkbox"
          />
          View as Admin (see all operations)
        </label>
      </div>
      
      <button @click="loadHistory" :disabled="!userId || isLoading" class="load-button">
        {{ isLoading ? 'Loading...' : 'Load History' }}
      </button>
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <div v-if="history.length > 0" class="history-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Status</th>
            <th>Objects Count</th>
            <th>Created At</th>
            <th>Error Message</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in history" :key="item.id" :class="{ 'failed': item.status === 'FAILED' }">
            <td>{{ item.id }}</td>
            <td>{{ item.userId }}</td>
            <td>
              <span :class="'status-badge status-' + item.status.toLowerCase()">
                {{ item.status }}
              </span>
            </td>
            <td>{{ item.objectCount || 'N/A' }}</td>
            <td>{{ formatDate(item.createdAt) }}</td>
            <td>{{ item.errorMessage || '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else-if="!isLoading && userId" class="no-data">
      No import history found
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const userId = ref('');
const isAdmin = ref(false);
const history = ref([]);
const isLoading = ref(false);
const errorMessage = ref('');

async function loadHistory() {
  if (!userId.value) {
    errorMessage.value = 'Please enter a user ID';
    return;
  }

  errorMessage.value = '';
  isLoading.value = true;

  try {
    const response = await fetch(
      `http://${import.meta.env.VITE_BACKEND_SOCKET}/api/ticket/import-history?userId=${encodeURIComponent(userId.value)}&isAdmin=${isAdmin.value}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      }
    );

    if (!response.ok) {
      throw new Error('Failed to load history');
    }

    history.value = await response.json();
  } catch (error) {
    errorMessage.value = error.message || 'Failed to load import history';
  } finally {
    isLoading.value = false;
  }
}

function formatDate(dateString) {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleString();
}
</script>

<style scoped>
.history-container {
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  color: #93c5fd; /* blue-300 */
  margin-bottom: 20px;
  font-size: 1.25rem;
}

.filters {
  background: #111827; /* gray-900 */
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
  display: flex;
  gap: 15px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.filter-group {
  flex: 1;
  min-width: 200px;
}

.filter-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #d1d5db; /* gray-300 */
}

.filter-group input[type="text"] {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 6px;
  font-size: 14px;
  background: #0f172a; /* slate-900 */
  color: #e5e7eb; /* gray-200 */
}

.filter-group input[type="text"]::placeholder {
  color: #6b7280; /* gray-500 */
}

.filter-group input[type="text"]:focus {
  outline: none;
  border-color: #2563eb; /* blue-600 */
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.25);
}

.filter-group input[type="checkbox"] {
  margin-right: 5px;
}

.load-button {
  padding: 10px 20px;
  background: #2563eb; /* blue-600 */
  color: #f9fafb; /* gray-50 */
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
  height: fit-content;
}

.load-button:hover:not(:disabled) {
  background: #1d4ed8; /* blue-700 */
}

.load-button:disabled {
  background: #374151; /* gray-700 */
  cursor: not-allowed;
  opacity: 0.5;
}

.error-message {
  padding: 10px;
  background: #7f1d1d; /* red-900 */
  border: 1px solid #991b1b; /* red-800 */
  border-radius: 6px;
  color: #fca5a5; /* red-300 */
  margin-bottom: 20px;
}

.history-table {
  background: #111827; /* gray-900 */
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #0f172a; /* slate-900 */
}

th {
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #e5e7eb; /* gray-200 */
  border-bottom: 1px solid #1f2937; /* gray-800 */
}

td {
  padding: 12px;
  border-bottom: 1px solid #0b1220; /* deep dark */
  color: #e5e7eb; /* gray-200 */
}

tr:hover {
  background: #0f172a; /* slate-900 */
}

tr.failed {
  background: #7f1d1d33; /* red-900 with transparency */
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
}

.status-success {
  background: #14532d; /* green-900 */
  color: #86efac; /* green-300 */
  border: 1px solid #166534; /* green-800 */
}

.status-failed {
  background: #7f1d1d; /* red-900 */
  color: #fca5a5; /* red-300 */
  border: 1px solid #991b1b; /* red-800 */
}

.no-data {
  padding: 40px;
  text-align: center;
  color: #6b7280; /* gray-500 */
  background: #111827; /* gray-900 */
  border: 1px solid #1f2937; /* gray-800 */
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
}
</style>
