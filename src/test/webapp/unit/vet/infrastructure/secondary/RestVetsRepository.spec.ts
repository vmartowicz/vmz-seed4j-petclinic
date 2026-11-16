import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { RestVetsRepository } from '@/vet/infrastructure/secondary/RestVetsRepository';
import { describe, expect, it } from 'vitest';
import { dataAxiosResponse, stubAxiosInstance } from '../../../shared/http/infrastructure/secondary/AxiosStub';
import { linda, vetsPage } from '../../domain/VetsFixture';

describe('Rest vets repository', () => {
  it('should list vets', async () => {
    const axios = stubAxiosInstance();
    axios.get.mockResolvedValue(dataAxiosResponse(vetsPage([linda()], 1, 2)));

    const page = await new RestVetsRepository(new AxiosHttp(axios)).list(1);

    expect(axios.get).toHaveBeenCalledWith('/api/vets', { params: { page: 1 } });
    expect(page).toEqual(vetsPage([linda()], 1, 2));
  });

  it('should reject with the problem detail', async () => {
    const axios = stubAxiosInstance();
    axios.get.mockRejectedValue({ message: 'Request failed', response: { data: { detail: 'Bad page' } } });

    await expect(new RestVetsRepository(new AxiosHttp(axios)).list(-1)).rejects.toThrow('Bad page');
  });
});
