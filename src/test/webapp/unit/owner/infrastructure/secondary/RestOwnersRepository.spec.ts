import { RestOwnersRepository } from '@/owner/infrastructure/secondary/RestOwnersRepository';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { describe, expect, it } from 'vitest';
import { dataAxiosResponse, stubAxiosInstance } from '../../../shared/http/infrastructure/secondary/AxiosStub';
import { george, leo } from '../../domain/OwnersFixture';

const ownerToSave = { firstName: 'George', lastName: 'Franklin', address: '110 W. Liberty St.', city: 'Madison', telephone: '6085551023' };
const petToSave = { name: 'Leo', birthDate: '2020-09-07', type: 'cat' };
const visitToCreate = { date: '2026-12-01', description: 'Check-up' };

const failure = { message: 'Request failed', response: { data: { detail: 'Owner is unknown' } } };

describe('Rest owners repository', () => {
  it('should search owners', async () => {
    const axios = stubAxiosInstance();
    axios.get.mockResolvedValue(dataAxiosResponse({ owners: [george()], currentPage: 1, pageCount: 2, totalElementsCount: 6 }));

    const page = await new RestOwnersRepository(new AxiosHttp(axios)).search('Fra', 1);

    expect(axios.get).toHaveBeenCalledWith('/api/owners', { params: { lastName: 'Fra', page: 1 } });
    expect(page).toEqual({ owners: [george()], currentPage: 1, pageCount: 2, totalElementsCount: 6 });
  });

  it('should get owner', async () => {
    const axios = stubAxiosInstance();
    axios.get.mockResolvedValue(dataAxiosResponse(george()));

    const owner = await new RestOwnersRepository(new AxiosHttp(axios)).get('george-id');

    expect(axios.get).toHaveBeenCalledWith('/api/owners/george-id', {});
    expect(owner).toEqual(george());
  });

  it('should register owner', async () => {
    const axios = stubAxiosInstance();
    axios.post.mockResolvedValue(dataAxiosResponse(george()));

    const owner = await new RestOwnersRepository(new AxiosHttp(axios)).register(ownerToSave);

    expect(axios.post).toHaveBeenCalledWith('/api/owners', ownerToSave, undefined);
    expect(owner).toEqual(george());
  });

  it('should update owner', async () => {
    const axios = stubAxiosInstance();
    axios.put.mockResolvedValue(dataAxiosResponse(george()));

    const owner = await new RestOwnersRepository(new AxiosHttp(axios)).update('george-id', ownerToSave);

    expect(axios.put).toHaveBeenCalledWith('/api/owners/george-id', ownerToSave);
    expect(owner).toEqual(george());
  });

  it('should add pet', async () => {
    const axios = stubAxiosInstance();
    axios.post.mockResolvedValue(dataAxiosResponse(leo()));

    const pet = await new RestOwnersRepository(new AxiosHttp(axios)).addPet('george-id', petToSave);

    expect(axios.post).toHaveBeenCalledWith('/api/owners/george-id/pets', petToSave, undefined);
    expect(pet).toEqual(leo());
  });

  it('should update pet', async () => {
    const axios = stubAxiosInstance();
    axios.put.mockResolvedValue(dataAxiosResponse(leo()));

    const pet = await new RestOwnersRepository(new AxiosHttp(axios)).updatePet('george-id', 'leo-id', petToSave);

    expect(axios.put).toHaveBeenCalledWith('/api/owners/george-id/pets/leo-id', petToSave);
    expect(pet).toEqual(leo());
  });

  it('should schedule visit', async () => {
    const axios = stubAxiosInstance();
    axios.post.mockResolvedValue(dataAxiosResponse({ id: 'visit-id', ...visitToCreate }));

    const visit = await new RestOwnersRepository(new AxiosHttp(axios)).scheduleVisit('george-id', 'leo-id', visitToCreate);

    expect(axios.post).toHaveBeenCalledWith('/api/owners/george-id/pets/leo-id/visits', visitToCreate, undefined);
    expect(visit).toEqual({ id: 'visit-id', ...visitToCreate });
  });

  it('should list pet types', async () => {
    const axios = stubAxiosInstance();
    axios.get.mockResolvedValue(dataAxiosResponse(['cat', 'dog']));

    const types = await new RestOwnersRepository(new AxiosHttp(axios)).petTypes();

    expect(axios.get).toHaveBeenCalledWith('/api/pet-types', {});
    expect(types).toEqual(['cat', 'dog']);
  });

  describe('Failures', () => {
    const calls: [string, (repository: RestOwnersRepository) => Promise<unknown>][] = [
      ['search', repository => repository.search('', 0)],
      ['get', repository => repository.get('george-id')],
      ['register', repository => repository.register(ownerToSave)],
      ['update', repository => repository.update('george-id', ownerToSave)],
      ['addPet', repository => repository.addPet('george-id', petToSave)],
      ['updatePet', repository => repository.updatePet('george-id', 'leo-id', petToSave)],
      ['scheduleVisit', repository => repository.scheduleVisit('george-id', 'leo-id', visitToCreate)],
      ['petTypes', repository => repository.petTypes()],
    ];

    it.each(calls)('should reject %s with the problem detail', async (_, call) => {
      const axios = stubAxiosInstance();
      axios.get.mockRejectedValue(failure);
      axios.post.mockRejectedValue(failure);
      axios.put.mockRejectedValue(failure);

      await expect(call(new RestOwnersRepository(new AxiosHttp(axios)))).rejects.toThrow('Owner is unknown');
    });
  });
});
