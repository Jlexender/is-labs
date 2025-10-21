<script setup>
import { ref } from 'vue'

const API_BASE = 'http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket'

const minNumberResult = ref(null)
const minNumberLoading = ref(false)
const minNumberError = ref('')

async function findMinNumber() {
    minNumberLoading.value = true
    minNumberError.value = ''
    minNumberResult.value = null
    try {
        const response = await fetch(`${API_BASE}/min-number`)
        if (!response.ok) {
            throw new Error('Ticket not found or error')
        }
        minNumberResult.value = await response.json()
    } catch (e) {
        minNumberError.value = e.message
    } finally {
        minNumberLoading.value = false
    }
}

const countNumber = ref('')
const countResult = ref(null)
const countLoading = ref(false)
const countError = ref('')

async function countByNumberLessThan() {
    if (!countNumber.value) {
        countError.value = 'Please enter a number'
        return
    }
    countLoading.value = true
    countError.value = ''
    countResult.value = null
    try {
        const response = await fetch(`${API_BASE}/count-less-than/${countNumber.value}`)
        if (!response.ok) {
            throw new Error('Failed to count')
        }
        countResult.value = await response.json()
    } catch (e) {
        countError.value = e.message
    } finally {
        countLoading.value = false
    }
}

const commentPrefix = ref('')
const commentResults = ref([])
const commentLoading = ref(false)
const commentError = ref('')

async function findByCommentPrefix() {
    if (!commentPrefix.value.trim()) {
        commentError.value = 'Please enter a prefix'
        return
    }
    commentLoading.value = true
    commentError.value = ''
    commentResults.value = []
    try {
        const response = await fetch(`${API_BASE}/by-comment-prefix?prefix=${encodeURIComponent(commentPrefix.value)}`)
        if (!response.ok) {
            throw new Error('Failed to search')
        }
        commentResults.value = await response.json()
    } catch (e) {
        commentError.value = e.message
    } finally {
        commentLoading.value = false
    }
}

const sellTicketId = ref('')
const sellPrice = ref('')
const sellEyeColor = ref('')
const sellHairColor = ref('')
const sellNationality = ref('')
const sellBirthday = ref('')
const sellLocX = ref('')
const sellLocY = ref('')
const sellLocZ = ref('')
const sellLocName = ref('')
const sellResult = ref(null)
const sellLoading = ref(false)
const sellError = ref('')

async function sellTicket() {
    if (!sellTicketId.value || !sellPrice.value) {
        sellError.value = 'Ticket ID and Price are required'
        return
    }
    sellLoading.value = true
    sellError.value = ''
    sellResult.value = null
    try {
        const person = {
            eyeColor: sellEyeColor.value || null,
            hairColor: sellHairColor.value || null,
            nationality: sellNationality.value || null,
            birthday: sellBirthday.value ? new Date(sellBirthday.value).toISOString() : null,
            location: {
                x: sellLocX.value ? parseInt(sellLocX.value) : null,
                y: sellLocY.value ? parseInt(sellLocY.value) : 0,
                z: sellLocZ.value ? parseInt(sellLocZ.value) : 0,
                name: sellLocName.value || null
            }
        }
        const response = await fetch(`${API_BASE}/sell/${sellTicketId.value}?price=${sellPrice.value}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(person)
        })
        if (!response.ok) {
            const text = await response.text()
            throw new Error(text || 'Failed to sell ticket')
        }
        sellResult.value = await response.json()
        // Clear form
        sellTicketId.value = ''
        sellPrice.value = ''
        sellEyeColor.value = ''
        sellHairColor.value = ''
        sellNationality.value = ''
        sellBirthday.value = ''
        sellLocX.value = ''
        sellLocY.value = ''
        sellLocZ.value = ''
        sellLocName.value = ''
    } catch (e) {
        sellError.value = e.message
    } finally {
        sellLoading.value = false
    }
}
</script>

<template>
    <div class="special-ops">
        <h1>Special Operations</h1>

        <section class="operation">
            <h2>1. Find Ticket with Minimum Number</h2>
            <button @click="findMinNumber" :disabled="minNumberLoading">
                {{ minNumberLoading ? 'Loading...' : 'Find Min Number' }}
            </button>
            <div v-if="minNumberError" class="error">{{ minNumberError }}</div>
            <div v-if="minNumberResult" class="result">
                <h3>Result:</h3>
                <pre>{{ JSON.stringify(minNumberResult, null, 2) }}</pre>
            </div>
        </section>

        <section class="operation">
            <h2>2. Count Tickets with Number Less Than</h2>
            <input v-model="countNumber" type="number" placeholder="Enter number" />
            <button @click="countByNumberLessThan" :disabled="countLoading">
                {{ countLoading ? 'Loading...' : 'Count' }}
            </button>
            <div v-if="countError" class="error">{{ countError }}</div>
            <div v-if="countResult !== null" class="result">
                <h3>Count: {{ countResult }}</h3>
            </div>
        </section>

        <section class="operation">
            <h2>3. Find Tickets by Comment Prefix</h2>
            <input v-model="commentPrefix" type="text" placeholder="Enter prefix" />
            <button @click="findByCommentPrefix" :disabled="commentLoading">
                {{ commentLoading ? 'Loading...' : 'Search' }}
            </button>
            <div v-if="commentError" class="error">{{ commentError }}</div>
            <div v-if="commentResults.length > 0" class="result">
                <h3>Found {{ commentResults.length }} ticket(s):</h3>
                <div v-for="ticket in commentResults" :key="ticket.id" class="ticket-item">
                    <strong>ID {{ ticket.id }}:</strong> {{ ticket.name }} - {{ ticket.comment }}
                </div>
            </div>
            <div v-else-if="commentResults.length === 0 && !commentLoading && !commentError && commentPrefix"
                class="result">
                No tickets found
            </div>
        </section>

        <section class="operation">
            <h2>4. Sell Ticket</h2>
            <div class="form-group">
                <label>Ticket ID:</label>
                <input v-model="sellTicketId" type="number" placeholder="Ticket ID" />
            </div>
            <div class="form-group">
                <label>Price:</label>
                <input v-model="sellPrice" type="number" step="0.01" placeholder="Sale price" />
            </div>
            <h4>Person Details:</h4>
            <div class="form-row">
                <input v-model="sellEyeColor" type="text" placeholder="Eye Color (e.g., BLUE)" />
                <input v-model="sellHairColor" type="text" placeholder="Hair Color (e.g., BLACK)" />
                <input v-model="sellNationality" type="text" placeholder="Nationality (e.g., RUSSIA)" />
            </div>
            <div class="form-row">
                <input v-model="sellBirthday" type="date" placeholder="Birthday" />
            </div>
            <h5>Location:</h5>
            <div class="form-row">
                <input v-model="sellLocX" type="number" placeholder="X" />
                <input v-model="sellLocY" type="number" placeholder="Y" />
                <input v-model="sellLocZ" type="number" placeholder="Z" />
                <input v-model="sellLocName" type="text" placeholder="Location name" />
            </div>
            <button @click="sellTicket" :disabled="sellLoading">
                {{ sellLoading ? 'Processing...' : 'Sell Ticket' }}
            </button>
            <div v-if="sellError" class="error">{{ sellError }}</div>
            <div v-if="sellResult" class="result">
                <h3>Ticket Sold!</h3>
                <pre>{{ JSON.stringify(sellResult, null, 2) }}</pre>
            </div>
        </section>
    </div>
</template>

<style scoped>
.special-ops {
    max-width: 1200px;
    margin: 0 auto;
}

h1 {
    color: #e5e7eb;
    margin-bottom: 24px;
    font-size: 1.75rem;
}

.operation {
    background: #111827; /* gray-900 */
    border: 1px solid #1f2937; /* gray-800 */
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 24px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
}

.operation h2 {
    color: #93c5fd; /* blue-300 */
    margin-bottom: 16px;
    font-size: 1.25rem;
}

.operation h4, .operation h5 {
    margin: 15px 0 10px 0;
    color: #9ca3af; /* gray-400 */
    font-size: 0.95rem;
}

.form-group {
    margin-bottom: 12px;
}

.form-group label {
    display: inline-block;
    width: 100px;
    font-weight: 600;
    color: #d1d5db; /* gray-300 */
}

.form-row {
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
}

.form-row input {
    flex: 1;
}

input {
    padding: 10px 12px;
    border: 1px solid #1f2937; /* gray-800 */
    border-radius: 6px;
    font-size: 14px;
    margin-right: 10px;
    margin-bottom: 10px;
    background: #0f172a; /* slate-900 */
    color: #e5e7eb; /* gray-200 */
}

input::placeholder {
    color: #6b7280; /* gray-500 */
}

input:focus {
    outline: none;
    border-color: #2563eb; /* blue-600 */
    box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.25);
}

button {
    padding: 10px 20px;
    background: #2563eb; /* blue-600 */
    color: #f9fafb; /* gray-50 */
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 14px;
    font-weight: 600;
    transition: background 0.2s;
}

button:hover:not(:disabled) {
    background: #1d4ed8; /* blue-700 */
}

button:disabled {
    background: #374151; /* gray-700 */
    cursor: not-allowed;
    opacity: 0.5;
}

button.danger {
    background: #dc2626; /* red-600 */
}

button.danger:hover:not(:disabled) {
    background: #b91c1c; /* red-700 */
}

.error {
    color: #fca5a5; /* red-300 */
    margin-top: 10px;
    padding: 10px;
    background: #7f1d1d; /* red-900 */
    border: 1px solid #991b1b; /* red-800 */
    border-radius: 6px;
}

.result {
    margin-top: 15px;
    padding: 15px;
    background: #0f172a; /* slate-900 */
    border-radius: 6px;
    border-left: 4px solid #2563eb; /* blue-600 */
    border: 1px solid #1e3a8a; /* blue-900 */
}

.result.success {
    border-left-color: #16a34a; /* green-600 */
    border-color: #14532d; /* green-900 */
    background: #052e16; /* green-950 */
}

.result h3 {
    margin-top: 0;
    margin-bottom: 12px;
    color: #93c5fd; /* blue-300 */
}

.result pre {
    background: #0b1220; /* deep dark */
    padding: 12px;
    border-radius: 6px;
    overflow-x: auto;
    font-size: 12px;
    color: #d1d5db; /* gray-300 */
    border: 1px solid #1f2937; /* gray-800 */
}

.ticket-item {
    padding: 10px;
    margin: 6px 0;
    background: #0f172a; /* slate-900 */
    border: 1px solid #1f2937; /* gray-800 */
    border-radius: 6px;
    color: #e5e7eb; /* gray-200 */
}

.ticket-item strong {
    color: #60a5fa; /* blue-400 */
}
</style>
