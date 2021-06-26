<template>
    <div class="loading-holder">
        <div class="spinner" v-if="data == null"><i class="fas fa-circle-notch fa-spin"></i></div>
        <div v-else-if="data.error">{{ errorMessage }}</div>
        <slot v-else></slot>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class SimpleLoader extends Vue {
    @Prop() readonly data!: string;
    @Prop() readonly errorMessage!: string;
}
</script>

<style scoped lang="scss">
.loading-holder .spinner {
    min-height: 150px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--category-color-secondary);
}
</style>