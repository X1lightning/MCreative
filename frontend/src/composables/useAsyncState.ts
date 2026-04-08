import { ref } from 'vue';

export function useAsyncState() {
  const loading = ref(false);
  const error = ref('');

  async function run<T>(promiseFactory: () => Promise<T>): Promise<T | undefined> {
    loading.value = true;
    error.value = '';
    try {
      return await promiseFactory();
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Something went wrong';
      return undefined;
    } finally {
      loading.value = false;
    }
  }

  return { loading, error, run };
}
