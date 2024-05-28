package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.data.RouteRepository;
import bg.softuni.pathfinder.model.Picture;
import bg.softuni.pathfinder.model.Route;
import bg.softuni.pathfinder.service.dto.RouteShortInfoDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private RouteRepository routRepository;
    private ModelMapper modelMapper;
    private Random random;

    public RouteService(RouteRepository routRepository, RouteRepository routeRepository) {
        this.routRepository = routRepository;
        this.modelMapper = new ModelMapper();
        this.random = new Random();
        this.routeRepository = routeRepository;
    }

    @Transactional
    public List<RouteShortInfoDto> getAll() {
        return routeRepository.findAll()
                .stream()
                .map(this::mapToShortInfo)
                .toList();
    }

    @Transactional
    public RouteShortInfoDto getRandomRoute() {
        long routeCount = routRepository.count();
        long randomId = random.nextLong(routeCount) + 1;
        Optional<Route> route = routRepository.findById(randomId);
        if (route.isEmpty()) {
            //throw exception
        }

        return mapToShortInfo(route.get());
    }


    private RouteShortInfoDto mapToShortInfo(Route route) {
        RouteShortInfoDto dto = modelMapper.map(route, RouteShortInfoDto.class);

        Optional<Picture> firstPicture = route.getPictures().stream().findFirst();
        dto.setImageUrl(firstPicture.get().getUrl());

        return dto;
    }

}
