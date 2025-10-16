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
const filterEye = ref('');
const filterHair = ref('');
const filterNationality = ref('');
const filterEventName = ref('');
const filterEventType = ref('');

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
    const eyeQ = filterEye.value.trim().toLowerCase();
    const hairQ = filterHair.value.trim().toLowerCase();
    const natQ = filterNationality.value.trim().toLowerCase();
    const eventNameQ = filterEventName.value.trim().toLowerCase();
    const eventTypeQ = filterEventType.value.trim().toLowerCase();
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
        const eyeStr = d.person && d.person.eyeColor ? String(d.person.eyeColor).toLowerCase() : '';
        const hairStr = d.person && d.person.hairColor ? String(d.person.hairColor).toLowerCase() : '';
        const natStr = d.person && d.person.nationality ? String(d.person.nationality).toLowerCase() : '';
        const eventNameStr = d.event && d.event.eventName ? String(d.event.eventName).toLowerCase() : '';
        const eventTypeStr = d.event && d.event.eventType ? String(d.event.eventType).toLowerCase() : '';

        if (idQ && !idStr.includes(idQ)) return false;
        if (nameQ && !nameStr.includes(nameQ)) return false;
        if (priceQ && !priceStr.includes(priceQ)) return false;
        if (typeQ && !typeStr.includes(typeQ)) return false;
        if (discountQ && !discountStr.includes(discountQ)) return false;
        if (numberQ && !numberStr.includes(numberQ)) return false;
        if (commentQ && !commentStr.includes(commentQ)) return false;
        if (coordXQ && !coordXStr.includes(coordXQ)) return false;
        if (coordYQ && !coordYStr.includes(coordYQ)) return false;
        if (eyeQ && !eyeStr.includes(eyeQ)) return false;
        if (hairQ && !hairStr.includes(hairQ)) return false;
        if (natQ && !natStr.includes(natQ)) return false;
        if (eventNameQ && !eventNameStr.includes(eventNameQ)) return false;
        if (eventTypeQ && !eventTypeStr.includes(eventTypeQ)) return false;
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

async function commitEdit(ticket) {
    if (editingId.value !== ticket.id) return;
    const payload = { ...ticket, [editingField.value]: draftValue.value };
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/ticket/' + ticket.id, {
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

async function removeTicket(id) {
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
                    <th>Eye</th>
                    <th>Hair</th>
                    <th>Nationality</th>
                    <th>Event Name</th>
                    <th>Event Type</th>
                    <th>Event Tickets</th>
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
                    <th><input type="text" v-model="filterEye" placeholder="Filter eye" /></th>
                    <th><input type="text" v-model="filterHair" placeholder="Filter hair" /></th>
                    <th><input type="text" v-model="filterNationality" placeholder="Filter nationality" /></th>
                    <th><input type="text" v-model="filterEventName" placeholder="Filter event name" /></th>
                    <th><input type="text" v-model="filterEventType" placeholder="Filter event type" /></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ticket in filteredTableData" :key="ticket.id">
                    <td>{{ ticket.id }}</td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'name')" @dblclick="startEdit(ticket.id, 'name', ticket.name)">{{ ticket.name }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'price')" @dblclick="startEdit(ticket.id, 'price', ticket.price)">{{ ticket.price }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'type')" @dblclick="startEdit(ticket.id, 'type', ticket.type)">{{ ticket.type }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'discount')" @dblclick="startEdit(ticket.id, 'discount', ticket.discount)">{{ ticket.discount }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'number')" @dblclick="startEdit(ticket.id, 'number', ticket.number)">{{ ticket.number }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>
                        <span v-if="!(editingId === ticket.id && editingField === 'comment')" @dblclick="startEdit(ticket.id, 'comment', ticket.comment)">{{ ticket.comment }}</span>
                        <input v-else v-model="draftValue" @keyup.enter="commitEdit(ticket)" @blur="cancelEdit" />
                    </td>
                    <td>{{ ticket.coordinates && ticket.coordinates.x }}</td>
                    <td>{{ ticket.coordinates && ticket.coordinates.y }}</td>
                    <td>{{ ticket.person && ticket.person.eyeColor }}</td>
                    <td>{{ ticket.person && ticket.person.hairColor }}</td>
                    <td>{{ ticket.person && ticket.person.nationality }}</td>
                    <td>{{ ticket.event && ticket.event.eventName }}</td>
                    <td>{{ ticket.event && ticket.event.eventType }}</td>
                    <td>{{ ticket.event && ticket.event.ticketsCount }}</td>
                    <td>
                        <button class="danger" @click="removeTicket(ticket.id)">Delete</button>
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