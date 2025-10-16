<script setup>
import { ref, inject } from 'vue';
import EntityPanel from '@/components/EntityTable.vue';

const tableData = inject('tableData');
const name = ref('');
const description = ref('');
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
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: name.value.trim(), description: description.value.trim() })
        });
        if (!response.ok) {
            throw new Error('Failed to create dummy');
        }
        await response.json();
        name.value = '';
        description.value = '';
    } catch (e) {
        errorMessage.value = e.message || 'Unknown error';
    } finally {
        isSubmitting.value = false;
    }
}
</script>

<template>
    <div class="home">
        <h1>Home View</h1>

        <form @submit.prevent="submit" class="create-form">
            <h2>Create Dummy</h2>
            <label for="name">Name</label>
            <input id="name" v-model="name" type="text" placeholder="Enter name" />

            <label for="description">Description</label>
            <input id="description" v-model="description" type="text" placeholder="Enter description" />

            <button type="submit" :disabled="isSubmitting">{{ isSubmitting ? 'Creating...' : 'Create' }}</button>
            <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        </form>

        <EntityPanel />
    </div>
</template>

<style scoped>
.create-form {
    display: grid;
    gap: 12px;
    margin-bottom: 16px;
    max-width: 420px;
}

.create-form label {
    font-weight: 600;
}

.create-form input[type="text"] {
    padding: 10px 12px;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
}

.create-form button {
    width: fit-content;
    padding: 10px 12px;
    background: #2563eb;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}

.error {
    color: #b91c1c;
}
</style>