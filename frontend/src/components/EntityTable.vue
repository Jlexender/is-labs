<script setup>
import { onMounted, inject, ref, computed } from 'vue';

const tableData = inject('tableData');

onMounted(async () => {
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket/all');
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
const filterPrice = ref('');
const filterType = ref('');
const filterDiscount = ref('');
const filterNumber = ref('');
const filterComment = ref('');
const filterCoordX = ref('');
const filterCoordY = ref('');

const filteredTableData = computed(() => {
    const idQ = filterId.value.trim().toLowerCase();
    const nameQ = filterName.value.trim().toLowerCase();
    const priceQ = filterPrice.value.trim().toLowerCase();
    const typeQ = filterType.value.trim().toLowerCase();
    const discountQ = filterDiscount.value.trim().toLowerCase();
    const numberQ = filterNumber.value.trim().toLowerCase();
    const commentQ = filterComment.value.trim().toLowerCase();
    const coordXQ = filterCoordX.value.trim().toLowerCase();
    const coordYQ = filterCoordY.value.trim().toLowerCase();
    return tableData.value.filter(d => {
        const idStr = d.id == null ? '' : String(d.id).toLowerCase();
        const nameStr = d.name == null ? '' : String(d.name).toLowerCase();
        const priceStr = d.price == null ? '' : String(d.price).toLowerCase();
        const typeStr = d.type == null ? '' : String(d.type).toLowerCase();
        const discountStr = d.discount == null ? '' : String(d.discount).toLowerCase();
        const numberStr = d.number == null ? '' : String(d.number).toLowerCase();
        const commentStr = d.comment == null ? '' : String(d.comment).toLowerCase();
        const coordXStr = d.coordinates && d.coordinates.x != null ? String(d.coordinates.x).toLowerCase() : '';
        const coordYStr = d.coordinates && d.coordinates.y != null ? String(d.coordinates.y).toLowerCase() : '';

        if (idQ && !idStr.includes(idQ)) return false;
        if (nameQ && !nameStr.includes(nameQ)) return false;
        if (priceQ && !priceStr.includes(priceQ)) return false;
        if (typeQ && !typeStr.includes(typeQ)) return false;
        if (discountQ && !discountStr.includes(discountQ)) return false;
        if (numberQ && !numberStr.includes(numberQ)) return false;
        if (commentQ && !commentStr.includes(commentQ)) return false;
        if (coordXQ && !coordXStr.includes(coordXQ)) return false;
        if (coordYQ && !coordYStr.includes(coordYQ)) return false;
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
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket/' + dummy.id, {
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
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket/' + id, {
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
        <h2>Tickets</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Type</th>
                    <th>Discount</th>
                    <th>Number</th>
                    <th>Comment</th>
                    <th>Coord X</th>
                    <th>Coord Y</th>
                    <th></th>
                </tr>
                <tr class="filters-row">
                    <th>
                        <input type="text" v-model="filterId" placeholder="Filter id" />
                    </th>
                    <th>
                        <input type="text" v-model="filterName" placeholder="Filter name" />
                    </th>
                    <th><input type="text" v-model="filterPrice" placeholder="Filter price" /></th>
                    <th><input type="text" v-model="filterType" placeholder="Filter type" /></th>
                    <th><input type="text" v-model="filterDiscount" placeholder="Filter discount" /></th>
                    <th><input type="text" v-model="filterNumber" placeholder="Filter number" /></th>
                    <th><input type="text" v-model="filterComment" placeholder="Filter comment" /></th>
                    <th><input type="text" v-model="filterCoordX" placeholder="Filter x" /></th>
                    <th><input type="text" v-model="filterCoordY" placeholder="Filter y" /></th>
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
                        <span v-if="!(editingId === dummy.id && editingField === 'price')" @dblclick="startEdit(dummy.id, 'price', dummy.price)">{{ dummy.price }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'type')" @dblclick="startEdit(dummy.id, 'type', dummy.type)">{{ dummy.type }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'discount')" @dblclick="startEdit(dummy.id, 'discount', dummy.discount)">{{ dummy.discount }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'number')" @dblclick="startEdit(dummy.id, 'number', dummy.number)">{{ dummy.number }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === dummy.id && editingField === 'comment')" @dblclick="startEdit(dummy.id, 'comment', dummy.comment)">{{ dummy.comment }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(dummy)" @blur="cancelEdit" />
                    </td>
                    <td>{{ dummy.coordinates && dummy.coordinates.x }}</td>
                    <td>{{ dummy.coordinates && dummy.coordinates.y }}</td>
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
    background: #0f172a;
    border-radius: 6px;
    overflow: hidden;
}

button.danger {
    padding: 6px 10px;
    background: #dc2626;
    color: #f9fafb;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}

thead .filters-row th {
    background: #0f172a;
}

thead input[type="text"] {
    width: 100%;
    padding: 6px 8px;
    border: 1px solid #1f2937;
    border-radius: 6px;
    background: #111827;
    color: #e5e7eb;
}
</style>