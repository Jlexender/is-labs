<script setup>
import { onMounted, inject, ref } from 'vue';

const tableData = inject('tableData');

onMounted(async () => {
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/all');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        tableData.value = await response.json();

        console.log('Fetched data:', tableData.value);
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
});

const editingId = ref(null);
const editingField = ref(null);
const draftValue = ref('');

function startEdit(id, field, currentValue) {
    editingId.value = id;
    editingField.value = field;
    draftValue.value = currentValue ?? '';
}

function cancelEdit() {
    editingId.value = null;
    editingField.value = null;
    draftValue.value = '';
}

async function commitEdit(dummy) {
    if (editingId.value !== dummy.id) return;
    const payload = { ...dummy, [editingField.value]: draftValue.value };
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/' + dummy.id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            throw new Error('Failed to update');
        }
        // Do not mutate local tableData; WS will update it
        cancelEdit();
    } catch (e) {
        console.error('Update error:', e);
    }
}

async function removeDummy(id) {
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/' + id, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('Failed to delete');
        }
        const idx = tableData.value.findIndex(d => d.id === id);
        if (idx !== -1) tableData.value.splice(idx, 1);
    } catch (e) {
        console.error('Delete error:', e);
    }
}
</script>

<template>
    <div class="entity-panel">
        <h2>Dummy Table</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Created at</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="dummy in tableData" :key="dummy.id">
                    <td>{{ dummy.id }}</td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'name')" @dblclick="startEdit(dummy.id, 'name', dummy.name)">{{ dummy.name }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'description')" @dblclick="startEdit(dummy.id, 'description', dummy.description)">{{ dummy.description }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>{{ dummy.createdAt }}</td>
                    <td>
                        <button class="danger" @click="removeDummy(dummy.id)">Delete</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>
.entity-panel {
    display: flex;
    flex-direction: column;
}

table {
    margin-top: 8px;
    background: white;
    border-radius: 6px;
    overflow: hidden;
}

button.danger {
    padding: 6px 10px;
    background: #ef4444;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}
</style>