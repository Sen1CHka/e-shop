package eshop.service;

import eshop.contracts.AddressRequest;
import eshop.contracts.AddressResponse;
import eshop.domain.Address;
import eshop.exception.ApiException;
import eshop.repository.AddressRepository;
import eshop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressResponse> getByUser(String username) {
        return findAll(username).stream()
                .map(AddressResponse::from)
                .toList();
    }

    @Transactional
    public AddressResponse create(String username, AddressRequest request) {
        List<Address> existing = findAll(username);
        Address address = new Address();
        address.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not found")));
        apply(address, request);
        // The first address is always the default one
        setDefault(address, existing, request.defaultAddress() || existing.isEmpty());
        return AddressResponse.from(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse update(Long id, String username, AddressRequest request) {
        Address address = find(id, username);
        apply(address, request);
        setDefault(address, findAll(username), request.defaultAddress() || address.isDefaultAddress());
        return AddressResponse.from(address);
    }

    @Transactional
    public void delete(Long id, String username) {
        Address address = find(id, username);
        addressRepository.delete(address);
        if (address.isDefaultAddress()) {
            findAll(username).stream()
                    .filter(other -> !other.getId().equals(id))
                    .findFirst()
                    .ifPresent(other -> other.setDefaultAddress(true));
        }
    }

    private List<Address> findAll(String username) {
        return addressRepository.findByUserUsernameOrderByDefaultAddressDescIdAsc(username);
    }

    private Address find(Long id, String username) {
        return addressRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> ApiException.notFound("Address not found"));
    }

    private void setDefault(Address address, List<Address> all, boolean makeDefault) {
        if (makeDefault) {
            all.forEach(other -> other.setDefaultAddress(false));
        }
        address.setDefaultAddress(makeDefault);
    }

    private void apply(Address address, AddressRequest request) {
        address.setFullName(request.fullName().trim());
        address.setStreet(request.street().trim());
        address.setCity(request.city().trim());
        address.setZipCode(request.zipCode().trim());
        address.setCountry(request.country().trim());
        address.setPhone(request.phone());
    }
}
