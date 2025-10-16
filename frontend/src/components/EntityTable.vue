<script setup>
import { onMounted, inject, ref, computed } from 'vue';

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

const filterId = ref('');
const filterName = ref('');
const filterDescription = ref('');
const filterCreatedAt = ref('');

const filteredTableData = computed(() => {
    const idQ = filterId.value.trim().toLowerCase();
    const nameQ = filterName.value.trim().toLowerCase();
    const descQ = filterDescription.value.trim().toLowerCase();
    const createdQ = filterCreatedAt.value.trim().toLowerCase();
    return tableData.value.filter(d => {
        const idStr = d.id == null ? '' : String(d.id).toLowerCase();
        const nameStr = d.name == null ? '' : String(d.name).toLowerCase();
        const descStr = d.description == null ? '' : String(d.description).toLowerCase();
        const createdStr = d.createdAt == null ? '' : String(d.createdAt).toLowerCase();

        if (idQ && !idStr.includes(idQ)) return false;
        if (nameQ && !nameStr.includes(nameQ)) return false;
        if (descQ && !descStr.includes(descQ)) return false;
        if (createdQ && !createdStr.includes(createdQ)) return false;
        return true;
    });
});

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
                <tr class="filters-row">
                    <th>
                        <input type="text" v-model="filterId" placeholder="Filter id" />
                    </th>
                    <th>
                        <input type="text" v-model="filterName" placeholder="Filter name" />
                    </th>
                    <th>
                        <input type="text" v-model="filterDescription" placeholder="Filter description" />
                    </th>
                    <th>
                        <input type="text" v-model="filterCreatedAt" placeholder="Filter created" />
                    </th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="dummy in filteredTableData" :key="dummy.id">
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

thead .filters-row th {
    background: #fff;
}

thead input[type="text"] {
    width: 100%;
    padding: 6px 8px;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
}
</style>