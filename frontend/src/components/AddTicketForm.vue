<script setup>
import { ref, inject } from 'vue';

const tableData = inject('tableData');
const name = ref('');
const price = ref('');
const type = ref('');
const discount = ref('');
const number = ref('');
const comment = ref('');
const coordX = ref('');
const coordY = ref('');
const isSubmitting = ref(false);
const errorMessage = ref('');

async function submit() {
    if (!name.value.trim()) {
        errorMessage.value = 'Name is required';
        return;
    }
    errorMessage.value = '';
    isSubmitting.value = true;
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name.value.trim(),
                coordinates: { x: parseFloat(coordX.value || 0), y: coordY.value === '' ? null : parseInt(coordY.value) },
                price: parseFloat(price.value || 0),
                type: type.value || null,
                discount: parseInt(discount.value || 0),
                number: parseInt(number.value || 0),
                comment: comment.value || null
            })
        });
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || 'Failed to create ticket');
        }
        await response.json();
        name.value = '';
        price.value = '';
        type.value = '';
        discount.value = '';
        number.value = '';
        comment.value = '';
        coordX.value = '';
        coordY.value = '';
    } catch (e) {
        errorMessage.value = e.message || 'Unknown error';
    } finally {
        isSubmitting.value = false;
    }
}
</script>

<template>
    <form @submit.prevent="submit" class="create-form">
        <label for="name">Name</label>
        <input id="name" v-model="name" type="text" placeholder="Enter name" />

        <label for="price">Price</label>
        <input id="price" v-model="price" type="number" step="0.01" placeholder="Enter price" />

        <label for="type">Type</label>
        <select id="type" v-model="type">
            <option value="">(none)</option>
            <option value="VIP">VIP</option>
            <option value="BUDGETARY">BUDGETARY</option>
            <option value="CHEAP">CHEAP</option>
        </select>

        <label for="discount">Discount (1-100)</label>
        <input id="discount" v-model="discount" type="number" min="1" max="100" placeholder="Enter discount" />

        <label for="number">Number</label>
        <input id="number" v-model="number" type="number" min="1" placeholder="Enter number" />

        <label for="comment">Comment</label>
        <input id="comment" v-model="comment" type="text" placeholder="Enter comment" />

        <label>Coordinates</label>
        <div class="grid-2">
            <input v-model="coordX" type="number" step="0.01" placeholder="x (<=397)" />
            <input v-model="coordY" type="number" placeholder="y (<=315)" />
        </div>

        <button type="submit" :disabled="isSubmitting">{{ isSubmitting ? 'Creating...' : 'Create' }}</button>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </form>
</template>

<style scoped>
.create-form {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 12px 16px;
}

.grid-2 {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 12px;
}

/* Improve input visuals and focus states in dark theme */
input[type="text"],
input[type="number"],
select {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #1f2937;
    border-radius: 6px;
    background: #0f172a;
    color: #e5e7eb;
}

input::placeholder {
    color: #9ca3af;
}

input:focus,
select:focus {
    outline: none;
    border-color: #2563eb;
    box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.25);
}

label {
    align-self: end;
}

button[type="submit"] {
    padding: 10px 12px;
    background: #2563eb;
    color: #f9fafb;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}
</style>


