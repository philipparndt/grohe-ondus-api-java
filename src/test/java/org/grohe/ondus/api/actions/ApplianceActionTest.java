package org.grohe.ondus.api.actions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.grohe.ondus.api.actions.ApplianceAction.BaseApplianceList;
import org.grohe.ondus.api.actions.ApplianceAction.SenseApplianceList;
import org.grohe.ondus.api.actions.ApplianceAction.SenseGuardApplianceList;
import org.grohe.ondus.api.client.ApiClient;
import org.grohe.ondus.api.client.ApiResponse;
import org.grohe.ondus.api.model.*;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ApplianceActionTest {
    private ApiClient mockApiClient;
    private ApiResponse mockApiResponse;
    private Room room123;

    @Before
    public void createMocks() {
        mockApiClient = mock(ApiClient.class);
        when(mockApiClient.apiPath()).thenReturn("/v2/");
        mockApiResponse = mock(ApiResponse.class);
        Location location123 = new Location(123);
        room123 = new Room(123, location123);
    }

    @Test
    public void getAppliances_invalidResponse_returnsEmptyList() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(500);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        List<BaseAppliance> actualList = action.getAppliances(room123);

        assertEquals(0, actualList.size());
    }

    @Test
    public void getAppliances_validResponse_returnsListOfAppliances() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new BaseAppliance[]{new BaseAppliance(), new BaseAppliance()}));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        List<BaseAppliance> actualList = action.getAppliances(room123);

        assertEquals(2, actualList.size());
        actualList.forEach(appliance -> assertEquals(room123, appliance.getRoom()));
    }

    @Test
    public void getAppliances_v3_returnsListOfAppliances() throws Exception {
        ApiClient mockV3ApiClient = mock(ApiClient.class);
        when(mockV3ApiClient.apiPath()).thenReturn("/v3/");
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new BaseAppliance[]{new BaseAppliance(), new BaseAppliance()}));
        when(mockV3ApiClient.get(eq("/v3/iot/locations/123/rooms/123/appliances"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockV3ApiClient);

        List<BaseAppliance> actualList = action.getAppliances(room123);

        assertEquals(2, actualList.size());
        actualList.forEach(appliance -> assertEquals(room123, appliance.getRoom()));
    }

    @Test
    public void getAppliance_invalidId_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseAppliance> actual = action.getAppliance(room123, "123");

        assertFalse(actual.isPresent());
    }

    @Test
    public void getAppliance_arrayAsResponse() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        SenseGuardAppliance appliance = new SenseGuardAppliance("123", new Room());
        appliance.setType(SenseGuardAppliance.TYPE);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), eq(BaseAppliance.class))).thenThrow(mock(MismatchedInputException.class));
        BaseApplianceList applianceList = new BaseApplianceList();
        applianceList.add(appliance);
        SenseGuardApplianceList senseGuardAppliances = new SenseGuardApplianceList();
        senseGuardAppliances.add(appliance);
        when(mockApiResponse.getContentAs(BaseApplianceList.class)).thenReturn(Optional.of(applianceList));
        when(mockApiResponse.getContentAs(SenseGuardApplianceList.class)).thenReturn(Optional.of(senseGuardAppliances));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), eq(List.class))).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseAppliance> actual = action.getAppliance(room123, "123");

        assertTrue(actual.isPresent());
    }

    @Test
    public void getAppliance_arrayWithoutItems_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), eq(BaseAppliance.class))).thenThrow(mock(MismatchedInputException.class));
        BaseApplianceList applianceList = new BaseApplianceList();
        when(mockApiResponse.getContentAs(BaseApplianceList.class)).thenReturn(Optional.of(applianceList));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), eq(List.class))).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseAppliance> actual = action.getAppliance(room123, "123");

        assertFalse(actual.isPresent());
    }

    @Test
    public void getAppliance_validId_returnsApplianceAsSenseGuard() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        BaseAppliance appliance = new BaseAppliance("123", new Room());
        appliance.setType(SenseGuardAppliance.TYPE);
        BaseApplianceList baseAppliances = new BaseApplianceList();
        baseAppliances.add(appliance);
        SenseGuardAppliance senseGuardAppliance = new SenseGuardAppliance("123", new Room());
        SenseGuardApplianceList senseGuardAppliances = new SenseGuardApplianceList();
        senseGuardAppliances.add(senseGuardAppliance);
        when(mockApiResponse.getContentAs(BaseApplianceList.class)).thenReturn(Optional.of(baseAppliances));
        when(mockApiResponse.getContentAs(SenseGuardApplianceList.class)).thenReturn(Optional.of(senseGuardAppliances));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseAppliance> actual = action.getAppliance(room123, "123");

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(room123, actual.get().getRoom());
        assertThat(actual.get(), instanceOf(SenseGuardAppliance.class));
    }

    @Test
    public void getAppliance_validId_returnsApplianceAsSense() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        BaseAppliance appliance = new BaseAppliance("123", new Room());
        appliance.setType(SenseAppliance.TYPE);
        BaseApplianceList baseAppliances = new BaseApplianceList();
        baseAppliances.add(appliance);
        SenseAppliance senseAppliance = new SenseAppliance("123", new Room());
        SenseApplianceList senseAppliances = new SenseApplianceList();
        senseAppliances.add(senseAppliance);
        when(mockApiResponse.getContentAs(BaseApplianceList.class)).thenReturn(Optional.of(baseAppliances));
        when(mockApiResponse.getContentAs(SenseApplianceList.class)).thenReturn(Optional.of(senseAppliances));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseAppliance> actual = action.getAppliance(room123, "123");

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(room123, actual.get().getRoom());
        assertThat(actual.get(), instanceOf(SenseAppliance.class));
    }

    @Test
    public void getApplianceData_invalidAppliance_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/data"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<BaseApplianceData> actual = action.getApplianceData(new SenseGuardAppliance("123", room123));

        assertFalse(actual.isPresent());
    }

    @Test
    public void getApplianceData_validAppliance_returnsApplianceData() throws Exception {
        mockApplianceDataResponse("");

        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);
        SenseGuardAppliance appliance = new SenseGuardAppliance("123", room123);

        Optional<BaseApplianceData> actual = action.getApplianceData(appliance);

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(appliance, actual.get().getAppliance());
    }

    @Test
    public void getApplianceData_validApplianceWithRange_returnsApplianceData() throws Exception {
        mockApplianceDataResponse("?from=2018-06-15&to=2018-06-18");

        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);
        SenseGuardAppliance appliance = new SenseGuardAppliance("123", room123);

        Optional<BaseApplianceData> actual = action.getApplianceData(appliance, Instant.parse("2018-06-15T13:00:00.00Z"), Instant.parse("2018-06-18T13:00:00.00Z"));

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(appliance, actual.get().getAppliance());
    }

    @Test
    public void getApplianceCommand_invalidApliance_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/command"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceCommand> actual = action.getApplianceCommand(new SenseGuardAppliance("123", room123));

        assertFalse(actual.isPresent());
    }

    @Test
    public void getApplianceCommand_validAppliance_returnsApplianceCommand() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        SenseGuardAppliance appliance = new SenseGuardAppliance("123", room123);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new ApplianceCommand(appliance)));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/command"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceCommand> actual = action.getApplianceCommand(appliance);

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
        assertEquals(appliance, actual.get().getAppliance());
    }

    @Test
    public void getApplianceStatus_invalidApliance_returnsEmptyOptional() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(404);
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/status"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceStatus> actual = action.getApplianceStatus(new BaseAppliance("123", room123));

        assertFalse(actual.isPresent());
    }

    @Test
    public void getApplianceStatus_validAppliance_returnsApplianceStatus() throws Exception {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        BaseAppliance appliance = new BaseAppliance("123", room123);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(new ApplianceStatus.ApplianceStatusModel[]{}));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/status"), any())).thenReturn(mockApiResponse);
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        Optional<ApplianceStatus> actual = action.getApplianceStatus(appliance);

        assertTrue(actual.isPresent());
        assertEquals("123", actual.get().getApplianceId());
    }

    @Test
    public void putApplianceCommand_validAppliance_callsCommandApi() throws Exception {
        SenseGuardAppliance appliance = new SenseGuardAppliance("123", room123);
        ApplianceCommand command = new ApplianceCommand(appliance);
        command.setCommand(new ApplianceCommand.Command());
        ApplianceAction action = new ApplianceAction();
        action.setApiClient(mockApiClient);

        action.putApplianceCommand(appliance, command);

        verify(mockApiClient).post(eq("/v2/iot/locations/123/rooms/123/appliances/123/command"),
                eq(command), eq(ApplianceCommand.class));
    }

    private void mockApplianceDataResponse(String rangeText) throws java.io.IOException {
        when(mockApiResponse.getStatusCode()).thenReturn(200);
        SenseGuardApplianceData applianceData = new SenseGuardApplianceData("123", new SenseGuardAppliance());
        applianceData.setType(103);
        when(mockApiResponse.getContent()).thenReturn(Optional.of(applianceData));
        when(mockApiResponse.getContentAs(any())).thenReturn(Optional.of(applianceData));
        when(mockApiClient.get(eq("/v2/iot/locations/123/rooms/123/appliances/123/data" +
                rangeText), any())).thenReturn(mockApiResponse);
    }
}