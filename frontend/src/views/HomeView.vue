<script setup>
import AddTicketForm from '@/components/AddTicketForm.vue';
import EntityPanel from '@/components/EntityTable.vue';
</script>

<template>
    <div class="home">
        <div class="stack">
            <AddTicketForm />
            <div class="stack-item">
                <EntityPanel />
            </div>
        </div>
    </div>
</template>

<style scoped>
.stack {
    display: flex;
    flex-direction: column;
}

.stack-item {
    margin-top: 24px;
}
</style>